package com.example.practicalwork4_variant6.composables.lesson14

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicalwork4_variant6.composables.common.InfoRow
import com.example.practicalwork4_variant6.composables.common.ScreenTitle
import com.example.practicalwork4_variant6.composables.common.statusLabelColor
import com.example.practicalwork4_variant6.domain.BiogasStation
import com.example.practicalwork4_variant6.domain.StationStatus
import com.example.practicalwork4_variant6.viewmodel.StationListViewModel

@Composable
fun Lesson14Basic(viewModel: StationListViewModel = viewModel()) {
    val stations by viewModel.stations.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Базовий рівень: LazyColumn — список об'єктів мережі")
        Text("Усього об'єктів: ${stations.size}")
        LazyColumn {
            items(stations, key = { it.id }) { station ->
                Text("${station.name} — ${station.type.label}", modifier = Modifier.padding(vertical = 6.dp))
            }
        }
    }
}

@Composable
fun Lesson14Medium(viewModel: StationListViewModel = viewModel()) {
    val stations by viewModel.stations.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Середній рівень: картки (Card) зі стислою інформацією")
        LazyColumn {
            items(stations, key = { it.id }) { station ->
                StationCard(station, expanded = false, onClick = {})
            }
        }
    }
}

@Composable
fun Lesson14Advanced(viewModel: StationListViewModel = viewModel()) {
    val stations by viewModel.stations.collectAsState()
    val selectedId by viewModel.selectedStationId.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Просунутий рівень: інтерактивний список з деталями")
        LazyColumn {
            items(stations, key = { it.id }) { station ->
                StationCard(
                    station = station,
                    expanded = station.id == selectedId,
                    onClick = { viewModel.selectStation(station.id) }
                )
            }
        }
    }
}

@Composable
fun StationCard(station: BiogasStation, expanded: Boolean, onClick: () -> Unit) {
    val isCritical = station.status == StationStatus.OFFLINE
    val isWarning = station.status == StationStatus.WARNING || station.status == StationStatus.MAINTENANCE

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(station.name, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            InfoRow("Тип", station.type.label)
            InfoRow(
                "Статус", station.status.label,
                valueColor = statusLabelColor(isCritical = isCritical, isWarning = isWarning)
            )
            if (expanded) {
                InfoRow("Потужність", "${"%.1f".format(station.powerKw)} кВт")
                InfoRow("Концентрація CH4", "${station.methaneLevel}%")
                InfoRow("Координати", "${"%.4f".format(station.latitude)}, ${"%.4f".format(station.longitude)}")
            }
        }
    }
}
