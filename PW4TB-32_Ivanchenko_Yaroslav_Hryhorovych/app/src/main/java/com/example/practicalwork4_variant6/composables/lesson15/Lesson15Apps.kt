package com.example.practicalwork4_variant6.composables.lesson15

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicalwork4_variant6.composables.common.ScreenTitle
import com.example.practicalwork4_variant6.composables.lesson14.StationCard
import com.example.practicalwork4_variant6.domain.StationSortBy
import com.example.practicalwork4_variant6.domain.StationStatus
import com.example.practicalwork4_variant6.domain.StationType
import com.example.practicalwork4_variant6.viewmodel.StationListViewModel

@Composable
fun Lesson15Basic(viewModel: StationListViewModel = viewModel()) {
    val query by viewModel.searchQuery.collectAsState()
    val result by viewModel.processedStations.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Базовий рівень: пошук і сортування")
        OutlinedTextField(
            value = query,
            onValueChange = viewModel::updateSearchQuery,
            label = { Text("Пошук за назвою") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        Row {
            AssistChip(onClick = { viewModel.updateSortBy(StationSortBy.NAME) }, label = { Text("За назвою") })
            AssistChip(onClick = { viewModel.updateSortBy(StationSortBy.TYPE) }, label = { Text("За типом") })
        }
        Text("Знайдено: ${result.size}")
        LazyColumn {
            items(result, key = { it.id }) { station ->
                Text("${station.name} — ${station.type.label}", modifier = Modifier.padding(vertical = 6.dp))
            }
        }
    }
}

@Composable
fun Lesson15Medium(viewModel: StationListViewModel = viewModel()) {
    val query by viewModel.searchQuery.collectAsState()
    val filterType by viewModel.filterType.collectAsState()
    val result by viewModel.processedStations.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Середній рівень: комбінована фільтрація + пошук")
        OutlinedTextField(
            value = query,
            onValueChange = viewModel::updateSearchQuery,
            label = { Text("Пошук за назвою") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        Row {
            AssistChip(onClick = { viewModel.updateFilterType(null) }, label = { Text("Усі типи") })
            AssistChip(
                onClick = { viewModel.updateFilterType(StationType.REACTOR) },
                label = { Text("Реактори") }
            )
            AssistChip(
                onClick = { viewModel.updateFilterType(StationType.BIOGAS_PLANT) },
                label = { Text("Станції") }
            )
        }
        Text("Активний фільтр типу: ${filterType?.label ?: "немає"}, знайдено: ${result.size}")
        LazyColumn {
            items(result, key = { it.id }) { station -> StationCard(station, expanded = false, onClick = {}) }
        }
    }
}

@Composable
fun Lesson15Advanced(viewModel: StationListViewModel = viewModel()) {
    val query by viewModel.searchQuery.collectAsState()
    val filterStatus by viewModel.filterStatus.collectAsState()
    val sortBy by viewModel.sortBy.collectAsState()
    val result by viewModel.processedStations.collectAsState()
    val selectedId by viewModel.selectedStationId.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Просунутий рівень: багаторівнева фільтрація і сортування")
        OutlinedTextField(
            value = query,
            onValueChange = viewModel::updateSearchQuery,
            label = { Text("Пошук за назвою") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
        Row {
            AssistChip(onClick = { viewModel.updateFilterStatus(null) }, label = { Text("Усі статуси") })
            AssistChip(
                onClick = { viewModel.updateFilterStatus(StationStatus.ONLINE) },
                label = { Text("У роботі") }
            )
            AssistChip(
                onClick = { viewModel.updateFilterStatus(StationStatus.WARNING) },
                label = { Text("Увага") }
            )
        }
        Row {
            AssistChip(onClick = { viewModel.updateSortBy(StationSortBy.POWER) }, label = { Text("↓ Потужність") })
            AssistChip(onClick = { viewModel.updateSortBy(StationSortBy.METHANE) }, label = { Text("↓ CH4") })
        }
        Text("Статус: ${filterStatus?.label ?: "немає"}, сортування: $sortBy, знайдено: ${result.size}")
        LazyColumn {
            items(result, key = { it.id }) { station ->
                StationCard(
                    station = station,
                    expanded = station.id == selectedId,
                    onClick = { viewModel.selectStation(station.id) }
                )
            }
        }
    }
}
