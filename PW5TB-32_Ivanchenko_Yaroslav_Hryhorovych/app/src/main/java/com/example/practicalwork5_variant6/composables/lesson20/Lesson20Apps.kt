package com.example.practicalwork5_variant6.composables.lesson20

import android.hardware.Sensor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.practicalwork5_variant6.ui.theme.StatusCritical
import com.example.practicalwork5_variant6.ui.theme.StatusOk
import com.example.practicalwork5_variant6.ui.theme.StatusWarning
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.min

@Composable
fun Lesson20Basic() {
    val values by rememberSensorValues(Sensor.TYPE_ACCELEROMETER)

    Column(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Базовий рівень: індикатор нахилу мішалки", style = MaterialTheme.typography.titleMedium)
        Canvas(modifier = Modifier.size(200.dp)) {
            val radius = size.minDimension / 2
            drawCircle(color = Color.LightGray, radius = radius, style = Stroke(width = 3f))
            val dotX = (size.width / 2) + (values[0] / 10f) * radius
            val dotY = (size.height / 2) - (values[1] / 10f) * radius
            drawCircle(color = Color(0xFF6650a4), radius = 16f, center = Offset(dotX, dotY))
        }
        Text("X: ${"%.2f".format(values[0])}  Y: ${"%.2f".format(values[1])}")
    }
}

@Composable
fun Lesson20Medium() {
    val values by rememberSensorValues(Sensor.TYPE_PRESSURE)
    val pressureKpa = (((values[0] - 1000f).coerceIn(-50f, 50f)) / 10f + 2.5f).coerceIn(0f, 5f)

    Column(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Середній рівень: шкала тиску біогазу", style = MaterialTheme.typography.titleMedium)
        Canvas(modifier = Modifier.size(200.dp)) {
            val strokeWidth = 24f
            val radius = min(size.width, size.height) / 2 - strokeWidth
            val topLeft = Offset(size.width / 2 - radius, size.height / 2 - radius)
            val arcSize = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)

            drawArc(
                color = Color.LightGray, startAngle = 135f, sweepAngle = 270f,
                useCenter = false, topLeft = topLeft, size = arcSize,
                style = Stroke(width = strokeWidth)
            )
            val sweep = (pressureKpa / 5f) * 270f
            val color = when {
                pressureKpa > 4f -> StatusCritical
                pressureKpa > 3f -> StatusWarning
                else -> StatusOk
            }
            drawArc(
                color = color, startAngle = 135f, sweepAngle = sweep,
                useCenter = false, topLeft = topLeft, size = arcSize,
                style = Stroke(width = strokeWidth)
            )
        }
        Text("${"%.2f".format(pressureKpa)} кПа")
    }
}

@Composable
fun Lesson20Advanced() {
    val accel by rememberSensorValues(Sensor.TYPE_ACCELEROMETER)
    val gyro by rememberSensorValues(Sensor.TYPE_GYROSCOPE)
    var scale by remember { mutableStateOf(1.0f) }
    var needleColor by remember { mutableStateOf(Color(0xFF6650a4)) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Просунутий рівень: орієнтація обладнання", style = MaterialTheme.typography.titleMedium)

        Canvas(modifier = Modifier.size(220.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2 - 10f
            drawCircle(color = Color.LightGray, radius = radius, center = center, style = Stroke(width = 3f))

            val angleRad = Math.atan2(accel[0].toDouble(), accel[1].toDouble())
            val length = (radius * 0.8f * scale).coerceAtMost(radius)
            val gyroMagnitude = (kotlin.math.abs(gyro[0]) + kotlin.math.abs(gyro[1]) + kotlin.math.abs(gyro[2]))
            val needleLength = (length * (0.6f + gyroMagnitude.coerceAtMost(2f) / 2f)).coerceAtMost(radius)

            val endX = center.x + (sin(angleRad) * needleLength).toFloat()
            val endY = center.y - (cos(angleRad) * needleLength).toFloat()
            drawLine(color = needleColor, start = center, end = Offset(endX, endY), strokeWidth = 8f)
            drawCircle(color = needleColor, radius = 10f, center = center)
        }

        Text("Масштаб чутливості: ${"%.1f".format(scale)}")
        Slider(value = scale, onValueChange = { scale = it }, valueRange = 0.5f..2f)

        Text("Колір стрілки:")
        androidx.compose.foundation.layout.Row {
            listOf(
                "Фіолетовий" to Color(0xFF6650a4),
                "Зелений" to StatusOk,
                "Жовтий" to StatusWarning,
                "Червоний" to StatusCritical
            ).forEach { (label, c) ->
                androidx.compose.material3.TextButton(onClick = { needleColor = c }) {
                    Text(label)
                }
            }
        }
    }
}
