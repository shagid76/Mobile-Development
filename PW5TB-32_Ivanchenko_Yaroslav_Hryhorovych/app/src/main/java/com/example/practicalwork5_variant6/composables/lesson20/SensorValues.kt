package com.example.practicalwork5_variant6.composables.lesson20

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberSensorValues(sensorType: Int): State<FloatArray> {
    val context = LocalContext.current
    val state = remember { mutableStateOf(floatArrayOf(0f, 0f, 0f)) }

    DisposableEffect(sensorType) {
        val sensorManager = context.getSystemService(SensorManager::class.java)
        val sensor: Sensor? = sensorManager.getDefaultSensor(sensorType)
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let { state.value = it.values.copyOf() }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sensor?.let { sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_UI) }
        onDispose { sensorManager.unregisterListener(listener) }
    }

    return state
}
