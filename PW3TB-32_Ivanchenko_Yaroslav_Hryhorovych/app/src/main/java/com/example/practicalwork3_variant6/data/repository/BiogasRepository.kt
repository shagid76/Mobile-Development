package com.example.practicalwork3_variant6.data.repository

import com.example.practicalwork3_variant6.data.local.LocalBiogasDataSource
import com.example.practicalwork3_variant6.data.local.ReactorReadingEntity
import com.example.practicalwork3_variant6.data.local.toDomain
import com.example.practicalwork3_variant6.data.remote.BiogasApiService
import com.example.practicalwork3_variant6.data.remote.CreateReadingRequest
import com.example.practicalwork3_variant6.data.remote.NetworkResult
import com.example.practicalwork3_variant6.data.remote.safeApiCall
import com.example.practicalwork3_variant6.data.remote.safeApiCallWithRetry
import com.example.practicalwork3_variant6.domain.ReactorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository для показників реактора — головний клас Заняття №9.
 *
 * Repository є єдиною точкою доступу до даних для [com.example.practicalwork3_variant6.viewmodel]:
 * ViewModel не знає, звідки саме приходять дані (з Room чи з мережі) — вона лише
 * викликає методи цього класу. Це і є суть патерну Repository + Data Source:
 * - [LocalBiogasDataSource] відповідає за локальне (офлайн) сховище Room;
 * - [BiogasApiService] (через Retrofit) — за віддалене джерело REST API;
 * - Repository агрегує обидва джерела, обирає оптимальне та приховує деталі
 *   від решти застосунку.
 */
class BiogasRepository(
    private val local: LocalBiogasDataSource,
    private val api: BiogasApiService
) {

    // ---------- Базовий рівень: лише локальне джерело ----------

    /** Потік усіх локально збережених показників (реактивне оновлення через Flow). */
    fun observeLocalReadings(): Flow<List<ReactorState>> =
        local.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun saveLocally(state: ReactorState) {
        local.save(
            ReactorReadingEntity(
                fillLevel = state.fillLevel,
                methaneConcentration = state.methaneConcentration,
                temperature = state.temperature,
                pressure = state.pressure,
                timestamp = System.currentTimeMillis(),
                isSynced = false
            )
        )
    }

    // ---------- Середній рівень: об'єднання локального та віддаленого джерел ----------

    /**
     * Завантажує показники з сервера і зберігає (кешує) їх локально, після чого
     * повертає локальний список — Repository сам вирішує, яке джерело "оптимальне":
     * якщо мережа недоступна, виклик поверне помилку, але локальні дані нікуди
     * не зникають і лишаються доступні через [observeLocalReadings].
     */
    suspend fun refreshFromServer(): NetworkResult<List<ReactorState>> {
        val result = safeApiCall { api.getReadings() }
        return when (result) {
            is NetworkResult.Success -> {
                val entities = result.data.map {
                    ReactorReadingEntity(
                        id = 0, // 0 => Room згенерує новий автоінкрементний id
                        fillLevel = it.fillLevel,
                        methaneConcentration = it.methaneConcentration,
                        temperature = it.temperature,
                        pressure = it.pressure,
                        timestamp = it.timestamp,
                        isSynced = true
                    )
                }
                local.saveAll(entities)
                NetworkResult.Success(result.data.map { it.toDomain() })
            }
            is NetworkResult.Error -> result
            NetworkResult.Loading -> NetworkResult.Loading
        }
    }

    suspend fun createOnServer(state: ReactorState): NetworkResult<ReactorState> {
        val request = CreateReadingRequest(
            fillLevel = state.fillLevel,
            methaneConcentration = state.methaneConcentration,
            temperature = state.temperature,
            pressure = state.pressure
        )
        val result = safeApiCall { api.createReading(request) }
        if (result is NetworkResult.Success) {
            saveLocally(state) // кешуємо локально одразу після успішного збереження на сервері
            return NetworkResult.Success(result.data.toDomain())
        }
        return result as NetworkResult<ReactorState>
    }

    // ---------- Просунутий рівень: агрегація, бізнес-логіка, повторні спроби ----------

    /**
     * Завантажує показники з сервера з повторними спробами при тимчасових
     * помилках (5xx, таймаут) і повертає агреговану бізнес-логіку —
     * середню концентрацію метану за всіма отриманими показниками.
     */
    suspend fun refreshWithRetryAndAggregate(): NetworkResult<Pair<List<ReactorState>, Double>> {
        val result = safeApiCallWithRetry(maxAttempts = 3) { api.getReadings() }
        return when (result) {
            is NetworkResult.Success -> {
                val states = result.data.map { it.toDomain() }
                local.saveAll(result.data.map { dto ->
                    ReactorReadingEntity(
                        fillLevel = dto.fillLevel,
                        methaneConcentration = dto.methaneConcentration,
                        temperature = dto.temperature,
                        pressure = dto.pressure,
                        timestamp = dto.timestamp,
                        isSynced = true
                    )
                })
                val averageMethane = states.map { it.methaneConcentration }.average()
                NetworkResult.Success(states to averageMethane)
            }
            is NetworkResult.Error -> result
            NetworkResult.Loading -> NetworkResult.Loading
        }
    }

    /** Сортування та фільтрація вже завантажених показників — бізнес-логіка рівня Repository. */
    fun sortByMethaneDescending(states: List<ReactorState>): List<ReactorState> =
        states.sortedByDescending { it.methaneConcentration }

    fun filterCritical(states: List<ReactorState>): List<ReactorState> =
        states.filter { it.methaneConcentration !in 50..70 }
}
