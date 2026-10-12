package com.example.practicalwork3_variant6

import com.example.practicalwork3_variant6.data.local.LocalBiogasDataSource
import com.example.practicalwork3_variant6.data.local.ReactorReadingDao
import com.example.practicalwork3_variant6.data.local.ReactorReadingEntity
import com.example.practicalwork3_variant6.data.remote.BiogasApiService
import com.example.practicalwork3_variant6.data.remote.CreateReadingRequest
import com.example.practicalwork3_variant6.data.remote.LoginRequest
import com.example.practicalwork3_variant6.data.remote.LoginResponse
import com.example.practicalwork3_variant6.data.remote.ReactorReadingDto
import com.example.practicalwork3_variant6.data.remote.SecureStatusResponse
import com.example.practicalwork3_variant6.data.remote.UnstableForecastResponse
import com.example.practicalwork3_variant6.data.repository.BiogasRepository
import com.example.practicalwork3_variant6.domain.ReactorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

private class FakeDao : ReactorReadingDao {
    override fun getAll(): Flow<List<ReactorReadingEntity>> = flowOf(emptyList())
    override suspend fun getById(id: Int): ReactorReadingEntity? = null
    override suspend fun insert(reading: ReactorReadingEntity): Long = 0
    override suspend fun insertAll(readings: List<ReactorReadingEntity>) {}
    override suspend fun delete(reading: ReactorReadingEntity) {}
    override suspend fun deleteAll() {}
}

private class FakeApi : BiogasApiService {
    override suspend fun getReadings(): Response<List<ReactorReadingDto>> =
        throw NotImplementedError("не використовується у цих тестах")
    override suspend fun getReadingById(id: Int): Response<ReactorReadingDto> =
        throw NotImplementedError("не використовується у цих тестах")
    override suspend fun createReading(request: CreateReadingRequest): Response<ReactorReadingDto> =
        throw NotImplementedError("не використовується у цих тестах")
    override suspend fun login(request: LoginRequest): Response<LoginResponse> =
        throw NotImplementedError("не використовується у цих тестах")
    override suspend fun getSecureStatus(): Response<SecureStatusResponse> =
        throw NotImplementedError("не використовується у цих тестах")
    override suspend fun getUnstableForecast(): Response<UnstableForecastResponse> =
        throw NotImplementedError("не використовується у цих тестах")
}

class BiogasRepositoryTest {

    private val repository = BiogasRepository(LocalBiogasDataSource(FakeDao()), FakeApi())

    @Test
    fun `sortByMethaneDescending orders readings from highest to lowest methane`() {
        val input = listOf(
            ReactorState(methaneConcentration = 40),
            ReactorState(methaneConcentration = 90),
            ReactorState(methaneConcentration = 60)
        )
        val sorted = repository.sortByMethaneDescending(input)
        assertEquals(listOf(90, 60, 40), sorted.map { it.methaneConcentration })
    }

    @Test
    fun `sortByMethaneDescending on empty list returns empty list`() {
        assertEquals(emptyList<ReactorState>(), repository.sortByMethaneDescending(emptyList()))
    }

    @Test
    fun `filterCritical keeps only readings outside the 50 to 70 stable range`() {
        val input = listOf(
            ReactorState(methaneConcentration = 20),  // критичний
            ReactorState(methaneConcentration = 55),  // в нормі
            ReactorState(methaneConcentration = 70),  // в нормі (межа)
            ReactorState(methaneConcentration = 95)   // критичний
        )
        val critical = repository.filterCritical(input)
        assertEquals(listOf(20, 95), critical.map { it.methaneConcentration })
    }

    @Test
    fun `filterCritical returns empty list when all readings are stable`() {
        val input = listOf(
            ReactorState(methaneConcentration = 55),
            ReactorState(methaneConcentration = 65)
        )
        assertEquals(emptyList<ReactorState>(), repository.filterCritical(input))
    }
}
