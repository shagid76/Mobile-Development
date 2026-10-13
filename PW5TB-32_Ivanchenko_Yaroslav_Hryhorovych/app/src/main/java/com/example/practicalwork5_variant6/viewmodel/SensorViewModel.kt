package com.example.practicalwork5_variant6.viewmodel

import android.app.Application
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicalwork5_variant6.domain.ProcessReading
import com.example.practicalwork5_variant6.domain.ReactorSensorSource
import com.example.practicalwork5_variant6.domain.ReactorThresholds
import com.example.practicalwork5_variant6.domain.ReadingType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SensorViewModel(application: Application) : AndroidViewModel(application) {
    private val sensorManager =
        application.getSystemService(Application.SENSOR_SERVICE) as SensorManager
    private val source = ReactorSensorSource(sensorManager)

    val temperatureAvailable: Boolean get() = source.temperatureSensor != null
    val pressureAvailable: Boolean get() = source.pressureSensor != null

    private val _temperature = MutableStateFlow<ProcessReading?>(null)
    val temperature: StateFlow<ProcessReading?> = _temperature.asStateFlow()

    private val _gasPressure = MutableStateFlow<ProcessReading?>(null)
    val gasPressure: StateFlow<ProcessReading?> = _gasPressure.asStateFlow()

    private val _generatorCurrent = MutableStateFlow<ProcessReading?>(null)
    val generatorCurrent: StateFlow<ProcessReading?> = _generatorCurrent.asStateFlow()

    private val _generatorVoltage = MutableStateFlow<ProcessReading?>(null)
    val generatorVoltage: StateFlow<ProcessReading?> = _generatorVoltage.asStateFlow()
    private val _activeAlerts = MutableStateFlow<List<ProcessReading>>(emptyList())
    val activeAlerts: StateFlow<List<ProcessReading>> = _activeAlerts.asStateFlow()
    private val _temperatureThreshold = MutableStateFlow(ReactorThresholds.TEMPERATURE_MAX_C)
    val temperatureThreshold: StateFlow<Float> = _temperatureThreshold.asStateFlow()

    fun setTemperatureThreshold(value: Float) {
        _temperatureThreshold.value = value
    }

    fun startCollecting() {
        viewModelScope.launch {
            source.temperatureFlow().collect { reading ->
                _temperature.value = reading
                updateAlerts(reading)
            }
        }
        viewModelScope.launch {
            source.gasPressureFlow().collect { reading ->
                _gasPressure.value = reading
                updateAlerts(reading)
            }
        }
        viewModelScope.launch {
            source.generatorCurrentFlow().collect { reading ->
                _generatorCurrent.value = reading
                updateAlerts(reading)
            }
        }
        viewModelScope.launch {
            source.generatorVoltageFlow().collect { reading ->
                _generatorVoltage.value = reading
            }
        }
    }

    private fun updateAlerts(reading: ProcessReading) {
        val exceeded = if (reading.type == ReadingType.TEMPERATURE) {
            reading.value > _temperatureThreshold.value
        } else {
            ReactorThresholds.isExceeded(reading)
        }
        val current = _activeAlerts.value.filterNot { it.type == reading.type }
        _activeAlerts.value = if (exceeded) current + reading else current
    }
}
