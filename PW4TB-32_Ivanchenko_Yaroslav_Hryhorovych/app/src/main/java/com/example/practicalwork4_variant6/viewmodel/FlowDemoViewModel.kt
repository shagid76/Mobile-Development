package com.example.practicalwork4_variant6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicalwork4_variant6.domain.StationDataSource

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlowDemoViewModel : ViewModel() {


    private val _basicReadings = MutableStateFlow<List<Int>>(emptyList())
    val basicReadings: StateFlow<List<Int>> = _basicReadings.asStateFlow()

    private val _basicRunning = MutableStateFlow(false)
    val basicRunning: StateFlow<Boolean> = _basicRunning.asStateFlow()

    fun startBasicCollection() {
        if (_basicRunning.value) return
        _basicReadings.value = emptyList()
        _basicRunning.value = true
        viewModelScope.launch {
            StationDataSource.sensorFlow().collect { value ->
                _basicReadings.update { it + value }
            }
            _basicRunning.value = false
        }
    }


    private val _mediumStatus = MutableStateFlow("Очікування даних…")
    val mediumStatus: StateFlow<String> = _mediumStatus.asStateFlow()

    private val _mediumPower = MutableStateFlow(0.0)
    val mediumPower: StateFlow<Double> = _mediumPower.asStateFlow()

    fun startMediumCollection() {
        _mediumStatus.value = "Очікування даних…"
        viewModelScope.launch {
            StationDataSource.sensorFlow()
                .filter { methane -> methane in 20..90 } // відкидаємо аномальні викиди
                .map { methane -> methane * 2.4 }         // умовне перетворення CH4% -> кВт
                .catch { e -> _mediumStatus.value = "Помилка потоку: ${e.message}" }
                .collect { power ->
                    _mediumPower.value = power
                    _mediumStatus.value = "Дані отримано успішно"
                }
        }
    }


    private val _advancedSensorPower = MutableStateFlow(0.0)
    val advancedSensorPower: StateFlow<Double> = _advancedSensorPower.asStateFlow()

    private val _advancedForecastPower = MutableStateFlow(0.0)
    val advancedForecastPower: StateFlow<Double> = _advancedForecastPower.asStateFlow()

    private val _advancedCombinedPower = MutableStateFlow(0.0)
    val advancedCombinedPower: StateFlow<Double> = _advancedCombinedPower.asStateFlow()

    private val _advancedStatus = MutableStateFlow("Очікування даних…")
    val advancedStatus: StateFlow<String> = _advancedStatus.asStateFlow()

    private val _liveTicker = MutableSharedFlow<Long>(replay = 0)
    val liveTicker = _liveTicker.asSharedFlow()

    fun startAdvancedCollection() {
        _advancedStatus.value = "Очікування даних…"
        viewModelScope.launch {
            val sensor = StationDataSource.sensorFlow().map { it.toDouble() }
            val forecast = StationDataSource.forecastFlow()
                .retry(retries = 2) { e ->
                    _advancedStatus.value = "Повторна спроба після помилки: ${e.message}"
                    true
                }
                .catch { e -> _advancedStatus.value = "Прогнозна модель недоступна: ${e.message}" }

            combine(sensor, forecast) { sensorValue, forecastValue ->
                sensorValue to forecastValue
            }.collect { (sensorValue, forecastValue) ->
                _advancedSensorPower.value = sensorValue
                _advancedForecastPower.value = forecastValue
                _advancedCombinedPower.value = (sensorValue + forecastValue) / 2
                _advancedStatus.value = "Дані успішно отримані та об'єднані"
                _liveTicker.emit(System.currentTimeMillis())
            }
        }
    }
}
