package com.example.practicalwork2_variant6.composables.lesson7

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicalwork2_variant6.composables.common.InfoRow
import com.example.practicalwork2_variant6.composables.common.ScreenTitle
import com.example.practicalwork2_variant6.composables.common.statusLabelColor
import com.example.practicalwork2_variant6.domain.ProcessStatus
import com.example.practicalwork2_variant6.domain.label
import com.example.practicalwork2_variant6.viewmodel.ReactorViewModel

@Composable
fun BasicViewModelApp(viewModel: ReactorViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Концентрація метану (StateFlow)")
        Text("${state.methaneConcentration}%", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        Button(
            onClick = { viewModel.updateMethane(state.methaneConcentration + 5) },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        ) { Text("+5%") }

        Button(
            onClick = { viewModel.updateMethane(state.methaneConcentration - 5) },
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
        ) { Text("-5%") }
    }
}

@Composable
fun MediumViewModelApp(viewModel: ReactorViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val status by viewModel.processStatus.collectAsState()
    val criticalAlerts by viewModel.criticalAlerts.observeAsState(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Стан реактора (MVVM)")
        InfoRow("Наповнення", "${state.fillLevel}%")
        InfoRow("Концентрація метану", "${state.methaneConcentration}%")
        InfoRow("Температура", "${state.temperature} °C")
        InfoRow("Статус процесу", status.label(), statusLabelColor(status == ProcessStatus.CRITICAL, status == ProcessStatus.WARNING))
        InfoRow("Критичних сповіщень (LiveData)", "$criticalAlerts")

        Button(
            onClick = { viewModel.randomizeReadings() },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        ) { Text("Оновити показники з давача") }
    }
}

@Composable
fun AdvancedViewModelApp(viewModel: ReactorViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val status by viewModel.processStatus.collectAsState()
    val criticalAlerts by viewModel.criticalAlerts.observeAsState(0)
    val history by viewModel.history.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        ScreenTitle("Реактивний моніторинг (MVVM)")

        InfoRow("Наповнення", "${state.fillLevel}%")
        InfoRow("Концентрація метану", "${state.methaneConcentration}%")
        InfoRow("Температура", "${state.temperature} °C")
        InfoRow("Тиск", "${state.pressure} кПа")
        InfoRow("Статус процесу", status.label(), statusLabelColor(status == ProcessStatus.CRITICAL, status == ProcessStatus.WARNING))
        InfoRow("Критичних сповіщень (LiveData)", "$criticalAlerts")

        Button(
            onClick = { viewModel.randomizeReadings() },
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
        ) { Text("Оновити показники з давача") }

        Text("Історія останніх показників метану:", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)

        LazyColumn(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            items(history.reversed()) { record ->
                val recordStatus = ProcessStatus.fromMethane(record.methaneConcentration)
                InfoRow(
                    label = "Метан",
                    value = "${record.methaneConcentration}% · ${recordStatus.label()}",
                    valueColor = statusLabelColor(recordStatus == ProcessStatus.CRITICAL, recordStatus == ProcessStatus.WARNING)
                )
            }
        }
    }
}
