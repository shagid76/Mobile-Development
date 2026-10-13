package com.example.practicalwork5_variant6.composables.lesson17

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.practicalwork5_variant6.domain.ProcessReading
import com.example.practicalwork5_variant6.ui.theme.StatusCritical
import com.example.practicalwork5_variant6.ui.theme.StatusOk
import com.example.practicalwork5_variant6.viewmodel.SensorViewModel

@Composable
fun Lesson17Basic(viewModel: SensorViewModel) {
    LaunchedEffect(Unit) { viewModel.startCollecting() }
    val temperature by viewModel.temperature.collectAsState()

    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text("Базовий рівень: Температура реактора", style = MaterialTheme.typography.titleMedium)
        if (!viewModel.temperatureAvailable) {
            Text("Сенсор температури недоступний на цьому пристрої — використайте емулятор із Virtual sensors (Additional sensors → Ambient temperature).")
        }
        LazyColumn {
            items(listOfNotNull(temperature)) { reading ->
                Text("Поточна величина: ${"%.1f".format(reading.value)} °C")
            }
        }
    }
}

@Composable
fun Lesson17Medium(viewModel: SensorViewModel) {
    LaunchedEffect(Unit) { viewModel.startCollecting() }
    val temperature by viewModel.temperature.collectAsState()
    val pressure by viewModel.gasPressure.collectAsState()

    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text("Середній рівень: Температура + Тиск біогазу", style = MaterialTheme.typography.titleMedium)
        ReadingCard("Температура реактора", temperature?.value, "°C")
        ReadingCard("Тиск біогазу", pressure?.value, "кПа")
    }
}

@Composable
fun Lesson17Advanced(viewModel: SensorViewModel) {
    LaunchedEffect(Unit) { viewModel.startCollecting() }
    val temperature by viewModel.temperature.collectAsState()
    val pressure by viewModel.gasPressure.collectAsState()
    val current by viewModel.generatorCurrent.collectAsState()
    val voltage by viewModel.generatorVoltage.collectAsState()
    val alerts by viewModel.activeAlerts.collectAsState()
    val threshold by viewModel.temperatureThreshold.collectAsState()

    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text("Просунутий рівень: Моніторинг сенсорів установки", style = MaterialTheme.typography.titleMedium)

        Text("Порогове значення температури для сповіщень: ${"%.1f".format(threshold)} °C")
        Slider(
            value = threshold,
            onValueChange = { viewModel.setTemperatureThreshold(it) },
            valueRange = 30f..50f
        )

        ReadingCard("Температура реактора", temperature?.value, "°C", isAlert(temperature, alerts))
        ReadingCard("Тиск біогазу", pressure?.value, "кПа", isAlert(pressure, alerts))
        ReadingCard("Струм когенератора", current?.value, "А", isAlert(current, alerts))
        ReadingCard("Напруга когенератора", voltage?.value, "В", isAlert(voltage, alerts))

        if (alerts.isNotEmpty()) {
            Text(
                "Увага: ${alerts.size} показник(ів) перевищують поріг!",
                color = StatusCritical,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

private fun isAlert(reading: ProcessReading?, alerts: List<ProcessReading>): Boolean =
    reading != null && alerts.any { it.type == reading.type }

@Composable
private fun ReadingCard(label: String, value: Float?, unit: String, alert: Boolean = false) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (alert) StatusCritical.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label)
            Text(
                text = value?.let { "${"%.1f".format(it)} $unit" } ?: "—",
                color = if (alert) StatusCritical else StatusOk
            )
        }
    }
}
