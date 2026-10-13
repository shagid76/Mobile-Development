package com.example.practicalwork5_variant6.composables.lesson19

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.practicalwork5_variant6.domain.PowerPoint
import com.example.practicalwork5_variant6.domain.ZonePower
import kotlin.math.max

@Composable
fun SimpleLineChart(
    points: List<PowerPoint>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF6650a4)
) {
    Canvas(modifier = modifier.fillMaxWidth().height(180.dp)) {
        if (points.isEmpty()) return@Canvas
        val maxY = max(points.maxOf { it.powerKw }, 1f)
        val minY = points.minOf { it.powerKw }
        val range = (maxY - minY).coerceAtLeast(1f)
        val stepX = size.width / (points.size - 1).coerceAtLeast(1)

        val path = androidx.compose.ui.graphics.Path()
        points.forEachIndexed { index, point ->
            val x = index * stepX
            val y = size.height - ((point.powerKw - minY) / range) * size.height
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, color = lineColor, style = Stroke(width = 4f))
    }
}

@Composable
fun SimpleBarChart(
    zones: List<ZonePower>,
    modifier: Modifier = Modifier,
    barColor: Color = Color(0xFF7D5260),
    onBarSelected: (ZonePower) -> Unit = {}
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .pointerInput(zones) {
                detectTapGestures { offset ->
                    if (zones.isEmpty()) return@detectTapGestures
                    val barWidth = size.width / zones.size
                    val index = (offset.x / barWidth).toInt().coerceIn(0, zones.size - 1)
                    onBarSelected(zones[index])
                }
            }
    ) {
        if (zones.isEmpty()) return@Canvas
        val maxValue = zones.maxOf { it.powerKw }.coerceAtLeast(1f)
        val barWidth = size.width / zones.size
        zones.forEachIndexed { index, zone ->
            val barHeight = (zone.powerKw / maxValue) * size.height
            drawRect(
                color = barColor,
                topLeft = Offset(index * barWidth + barWidth * 0.15f, size.height - barHeight),
                size = androidx.compose.ui.geometry.Size(barWidth * 0.7f, barHeight)
            )
        }
    }
}
