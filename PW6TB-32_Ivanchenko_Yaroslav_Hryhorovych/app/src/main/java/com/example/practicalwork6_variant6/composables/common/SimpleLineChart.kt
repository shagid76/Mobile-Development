package com.example.practicalwork6_variant6.composables.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun SimpleLineChart(
    values: List<Float>,
    modifier: Modifier = Modifier.fillMaxWidth().height(160.dp)
) {
    val lineColor = MaterialTheme.colorScheme.primary
    val pointColor = MaterialTheme.colorScheme.error
    Canvas(modifier = modifier) {
        if (values.isEmpty()) return@Canvas
        val maxV = (values.maxOrNull() ?: 1f).coerceAtLeast(1f)
        val minV = (values.minOrNull() ?: 0f).coerceAtMost(0f)
        val range = (maxV - minV).let { if (it == 0f) 1f else it }
        val stepX = if (values.size > 1) size.width / (values.size - 1) else size.width

        val points = values.mapIndexed { index, v ->
            Offset(
                x = index * stepX,
                y = size.height - ((v - minV) / range) * size.height
            )
        }
        for (i in 0 until points.size - 1) {
            drawLine(
                color = lineColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
        }
        points.forEach { p ->
            drawCircle(color = pointColor, radius = 6f, center = p, style = Stroke(width = 3f))
        }
    }
}
