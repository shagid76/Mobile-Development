package com.example.practicalwork5_variant6.domain

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random
class ReactorSensorSource(private val sensorManager: SensorManager) {

    val temperatureSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    val pressureSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    fun temperatureFlow(): Flow<ProcessReading> = sensorFlow(temperatureSensor) { event ->
        ProcessReading(ReadingType.TEMPERATURE, event.values[0])
    }
    fun gasPressureFlow(): Flow<ProcessReading> = sensorFlow(pressureSensor) { event ->
        val hpa = event.values[0]
        val normalized = ((hpa - 1000f).coerceIn(-50f, 50f)) / 10f + 2.5f
        ProcessReading(ReadingType.GAS_PRESSURE, normalized)
    }

    private fun sensorFlow(sensor: Sensor?, map: (SensorEvent) -> ProcessReading): Flow<ProcessReading> =
        callbackFlow {
            if (sensor == null) {
                close()
                return@callbackFlow
            }
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let { trySend(map(it)) }
                }
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            awaitClose { sensorManager.unregisterListener(listener) }
        }
    fun generatorCurrentFlow(periodMs: Long = 1500L): Flow<ProcessReading> = flow {
        var current = 14.0f
        while (true) {
            current = (current + Random.nextFloat() * 4f - 2f).coerceIn(5f, 30f)
            emit(ProcessReading(ReadingType.GENERATOR_CURRENT, current))
            delay(periodMs)
        }
    }
    fun generatorVoltageFlow(periodMs: Long = 1500L): Flow<ProcessReading> = flow {
        var voltage = 230.0f
        while (true) {
            voltage = (voltage + Random.nextFloat() * 6f - 3f).coerceIn(210f, 245f)
            emit(ProcessReading(ReadingType.GENERATOR_VOLTAGE, voltage))
            delay(periodMs)
        }
    }
}
