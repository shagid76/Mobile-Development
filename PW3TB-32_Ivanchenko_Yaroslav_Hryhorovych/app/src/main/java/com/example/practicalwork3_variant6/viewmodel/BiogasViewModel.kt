package com.example.practicalwork3_variant6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicalwork3_variant6.data.local.AppDatabase
import com.example.practicalwork3_variant6.data.local.LocalBiogasDataSource
import com.example.practicalwork3_variant6.data.remote.NetworkModule
import com.example.practicalwork3_variant6.data.remote.NetworkResult
import com.example.practicalwork3_variant6.data.repository.BiogasRepository
import com.example.practicalwork3_variant6.domain.ReactorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel для Занять №9-10: не містить мережевого чи Room-коду напряму —
 * уся робота з даними делегована [BiogasRepository] (патерн Repository/Data Source).
 */
class BiogasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BiogasRepository(
        local = LocalBiogasDataSource(AppDatabase.getInstance(application).reactorReadingDao()),
        api = NetworkModule.provideApiService(application)
    )

    /** Показники, що зберігаються локально (Room) — реактивно оновлюються через Flow. */
    val localReadings: StateFlow<List<ReactorState>> = repository.observeLocalReadings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _networkState = MutableStateFlow<NetworkResult<List<ReactorState>>?>(null)
    val networkState: StateFlow<NetworkResult<List<ReactorState>>?> = _networkState

    private val _averageMethane = MutableStateFlow<Double?>(null)
    val averageMethane: StateFlow<Double?> = _averageMethane

    fun saveLocally(state: ReactorState) {
        viewModelScope.launch { repository.saveLocally(state) }
    }

    /** Заняття №9, базовий рівень — лише локальне джерело, мережа не задіяна. */
    fun refreshLocalOnly() {
        // observeLocalReadings() вже реактивно оновлюється сама — тут нічого
        // додатково робити не потрібно, що й демонструє простоту базового рівня.
    }

    /** Заняття №9-10, середній рівень — об'єднання локального і віддаленого джерел. */
    fun refreshFromServer() {
        viewModelScope.launch {
            _networkState.value = NetworkResult.Loading
            _networkState.value = repository.refreshFromServer()
        }
    }

    fun createOnServer(state: ReactorState) {
        viewModelScope.launch {
            _networkState.value = NetworkResult.Loading
            val result = repository.createOnServer(state)
            _networkState.value = when (result) {
                is NetworkResult.Success -> repository.refreshFromServer()
                is NetworkResult.Error -> result
                NetworkResult.Loading -> NetworkResult.Loading
            }
        }
    }

    /** Заняття №9-10, просунутий рівень — повторні спроби та агрегація бізнес-логіки. */
    fun refreshWithRetryAndAggregate() {
        viewModelScope.launch {
            _networkState.value = NetworkResult.Loading
            when (val result = repository.refreshWithRetryAndAggregate()) {
                is NetworkResult.Success -> {
                    _networkState.value = NetworkResult.Success(result.data.first)
                    _averageMethane.value = result.data.second
                }
                is NetworkResult.Error -> _networkState.value = result
                NetworkResult.Loading -> _networkState.value = NetworkResult.Loading
            }
        }
    }

    fun sortByMethaneDescending(states: List<ReactorState>) = repository.sortByMethaneDescending(states)
    fun filterCritical(states: List<ReactorState>) = repository.filterCritical(states)
}
