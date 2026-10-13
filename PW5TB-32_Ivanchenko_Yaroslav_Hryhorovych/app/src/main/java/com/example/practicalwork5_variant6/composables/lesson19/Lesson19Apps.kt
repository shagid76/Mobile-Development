package com.example.practicalwork5_variant6.composables.lesson19

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practicalwork5_variant6.domain.PowerHistoryProcessor
import com.example.practicalwork5_variant6.domain.ZonePower

@Composable
fun Lesson19Basic() {
    val history = remember { PowerHistoryProcessor.generateHistory(20) }
    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text("Базовий рівень: графік потужності когенератора", style = MaterialTheme.typography.titleMedium)
        SimpleLineChart(points = history)
        Text("Потужність за останні ${history.size} хв, кВт")
    }
}

@Composable
fun Lesson19Medium() {
    val zones = remember { PowerHistoryProcessor.zoneComparison() }
    var selected by remember { mutableStateOf<ZonePower?>(null) }

    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text("Середній рівень: потужність по зонах установки", style = MaterialTheme.typography.titleMedium)
        SimpleBarChart(zones = zones, onBarSelected = { selected = it })
        Text(
            selected?.let { "${it.zoneName}: ${"%.1f".format(it.powerKw)} кВт" }
                ?: "Торкніться стовпчика, щоб побачити деталі"
        )
    }
}

@Composable
fun Lesson19Advanced() {
    val fullHistory = remember { PowerHistoryProcessor.generateHistory(60) }
    var windowMinutes by remember { mutableStateOf(30) }
    val window = remember(windowMinutes) { PowerHistoryProcessor.windowed(fullHistory, windowMinutes) }
    val zones = remember { PowerHistoryProcessor.zoneComparison() }
    var selectedZone by remember { mutableStateOf<ZonePower?>(null) }

    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text("Просунутий рівень: комплексний аналіз потужності", style = MaterialTheme.typography.titleMedium)

        Row(modifier = Modifier.fillMaxWidth()) {
            listOf(15, 30, 60).forEach { minutes ->
                Button(
                    onClick = { windowMinutes = minutes },
                    enabled = windowMinutes != minutes,
                    modifier = Modifier.padding(end = 4.dp)
                ) { Text("$minutes хв") }
            }
        }

        SimpleLineChart(points = window)
        Text("Середнє: ${"%.1f".format(PowerHistoryProcessor.average(window))} кВт" +
                "  •  Пік: ${"%.1f".format(PowerHistoryProcessor.peak(window)?.powerKw ?: 0f)} кВт")

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(12.dp))

        SimpleBarChart(zones = zones, onBarSelected = { selectedZone = it })
        Text(
            selectedZone?.let { "${it.zoneName}: ${"%.1f".format(it.powerKw)} кВт" }
                ?: "Торкніться стовпчика, щоб побачити деталі зони"
        )
    }
}
