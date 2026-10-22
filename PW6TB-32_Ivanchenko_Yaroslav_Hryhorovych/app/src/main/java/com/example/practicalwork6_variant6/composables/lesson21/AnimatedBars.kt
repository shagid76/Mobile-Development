package com.example.practicalwork6_variant6.composables.lesson21

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedBar(
    label: String,
    value: Float,
    maxValue: Float,
    unit: String,
    warning: Boolean = false,
    modifier: Modifier = Modifier
) {
    val fraction = (value / maxValue).coerceIn(0f, 1f)
    val barHeight by animateDpAsState(
        targetValue = (140.dp * fraction),
        animationSpec = spring(dampingRatio = 0.7f),
        label = "barHeight"
    )
    val barColor by animateColorAsState(
        targetValue = if (warning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        animationSpec = tween(400),
        label = "barColor"
    )

    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        Box(
            modifier = Modifier
                .height(150.dp)
                .width(40.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .height(barHeight)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(barColor)
            )
        }
        Text(
            text = "%.1f %s".format(value, unit),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun InteractiveBar(
    label: String,
    value: Float,
    maxValue: Float,
    unit: String,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fraction = (value / maxValue).coerceIn(0f, 1f)
    val animatedFraction by animateFloatAsState(
        targetValue = fraction,
        animationSpec = tween(500),
        label = "interactiveFraction"
    )
    Column(
        modifier = modifier
            .padding(8.dp)
            .clickable { onTap() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        Box(
            modifier = Modifier
                .height(150.dp)
                .width(44.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .height(150.dp * animatedFraction)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.tertiary)
            )
        }
        Text(text = "%.1f %s".format(value, unit), style = MaterialTheme.typography.labelMedium)
        Text(text = "Натисніть", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@Composable
fun BarRow(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        content()
    }
}
