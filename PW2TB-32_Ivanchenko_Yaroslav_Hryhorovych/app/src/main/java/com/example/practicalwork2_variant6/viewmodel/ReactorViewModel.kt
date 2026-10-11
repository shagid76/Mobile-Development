package com.example.practicalwork2_variant6.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicalwork2_variant6.domain.ProcessStatus
import com.example.practicalwork2_variant6.domain.ReactorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random

class ReactorViewModel : ViewModel() {

    private val _state = MutableStateFlow(ReactorState())
    val state: StateFlow<ReactorState> = _state.asStateFlow()

    val processStatus: StateFlow<ProcessStatus> = _state
        .map { ProcessStatus.fromMethane(it.methaneConcentration) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProcessStatus.STABLE)

    private val _criticalAlerts = MutableLiveData(0)
    val criticalAlerts: LiveData<Int> = _criticalAlerts

    private val _history = MutableStateFlow<List<ReactorState>>(emptyList())
    val history: StateFlow<List<ReactorState>> = _history.asStateFlow()

    fun updateFillLevel(value: Int) = updateState(_state.value.copy(fillLevel = value.coerceIn(0, 100)))
    fun updateMethane(value: Int) = updateState(_state.value.copy(methaneConcentration = value.coerceIn(0, 100)))
    fun updateTemperature(value: Int) = updateState(_state.value.copy(temperature = value.coerceIn(-10, 80)))
    fun updatePressure(value: Int) = updateState(_state.value.copy(pressure = value.coerceIn(0, 500)))

    fun randomizeReadings() {
        updateState(
            ReactorState(
                fillLevel = Random.nextInt(0, 101),
                methaneConcentration = Random.nextInt(0, 101),
                temperature = Random.nextInt(-10, 81),
                pressure = Random.nextInt(0, 501)
            )
        )
    }

    private fun updateState(newState: ReactorState) {
        _state.value = newState
        if (ProcessStatus.fromMethane(newState.methaneConcentration) == ProcessStatus.CRITICAL) {
            _criticalAlerts.value = (_criticalAlerts.value ?: 0) + 1
        }
        _history.value = (_history.value + newState).takeLast(10)
    }
}
