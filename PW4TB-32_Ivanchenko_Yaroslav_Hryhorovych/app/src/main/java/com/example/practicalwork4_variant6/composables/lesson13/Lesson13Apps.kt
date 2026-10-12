package com.example.practicalwork4_variant6.composables.lesson13

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicalwork4_variant6.composables.common.InfoRow
import com.example.practicalwork4_variant6.composables.common.MenuButton
import com.example.practicalwork4_variant6.composables.common.ScreenTitle
import com.example.practicalwork4_variant6.viewmodel.FlowDemoViewModel

@Composable
fun Lesson13Basic(viewModel: FlowDemoViewModel = viewModel()) {
    val readings by viewModel.basicReadings.collectAsState()
    val running by viewModel.basicRunning.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Базовий рівень: холодний Flow + корутина")
        MenuButton(if (running) "Збір даних…" else "Почати збір показників") {
            viewModel.startBasicCollection()
        }
        if (running) CircularProgressIndicator()
        Text("Отримано значень: ${readings.size}")
        LazyColumn {
            items(readings) { value -> InfoRow("CH4", "$value%") }
        }
    }
}

@Composable
fun Lesson13Medium(viewModel: FlowDemoViewModel = viewModel()) {
    val status by viewModel.mediumStatus.collectAsState()
    val power by viewModel.mediumPower.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Середній рівень: трансформація та обробка помилок")
        MenuButton("Почати збір з трансформацією") { viewModel.startMediumCollection() }
        Text("Статус: $status")
        InfoRow("Поточна потужність", "${"%.1f".format(power)} кВт")
    }
}

@Composable
fun Lesson13Advanced(viewModel: FlowDemoViewModel = viewModel()) {
    val status by viewModel.advancedStatus.collectAsState()
    val sensorPower by viewModel.advancedSensorPower.collectAsState()
    val forecastPower by viewModel.advancedForecastPower.collectAsState()
    val combinedPower by viewModel.advancedCombinedPower.collectAsState()
    var updateCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.liveTicker.collect { updateCount++ }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Просунутий рівень: combine() двох потоків + retry")
        MenuButton("Почати комбінований збір") { viewModel.startAdvancedCollection() }
        Text("Статус: $status")
        InfoRow("Потужність сенсора", "${"%.1f".format(sensorPower)} кВт")
        InfoRow("Прогнозна потужність", "${"%.1f".format(forecastPower)} кВт")
        InfoRow("Комбінована потужність", "${"%.1f".format(combinedPower)} кВт")
        Text("Оновлень отримано («гарячий» потік): $updateCount")
    }
}
