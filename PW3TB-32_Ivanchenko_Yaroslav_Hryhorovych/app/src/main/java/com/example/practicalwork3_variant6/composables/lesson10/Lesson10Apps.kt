package com.example.practicalwork3_variant6.composables.lesson10

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicalwork3_variant6.composables.common.InfoRow
import com.example.practicalwork3_variant6.composables.common.MenuButton
import com.example.practicalwork3_variant6.composables.common.ScreenTitle
import com.example.practicalwork3_variant6.composables.common.statusLabelColor
import com.example.practicalwork3_variant6.data.remote.NetworkResult
import com.example.practicalwork3_variant6.domain.ProcessStatus
import com.example.practicalwork3_variant6.domain.label
import com.example.practicalwork3_variant6.viewmodel.BiogasViewModel

@Composable
fun Lesson10Basic(viewModel: BiogasViewModel = viewModel()) {
    val networkState by viewModel.networkState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Базовий рівень: один REST-запит (Retrofit + Gson)")
        MenuButton("Завантажити показники") { viewModel.refreshFromServer() }

        when (val state = networkState) {
            is NetworkResult.Loading -> CircularProgressIndicator()
            is NetworkResult.Error -> Text("Помилка завантаження: ${state.message}")
            is NetworkResult.Success -> LazyColumn {
                items(state.data) { reading ->
                    Text("Метан: ${reading.methaneConcentration}%, наповнення: ${reading.fillLevel}%")
                }
            }
            null -> Text("Натисніть кнопку, щоб завантажити дані з сервера")
        }
    }
}

@Composable
fun Lesson10Medium(viewModel: BiogasViewModel = viewModel()) {
    val localReadings by viewModel.localReadings.collectAsState()
    val networkState by viewModel.networkState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Середній рівень: кешування і фільтрація JSON-даних")
        MenuButton("Оновити з сервера") { viewModel.refreshFromServer() }

        if (networkState is NetworkResult.Loading) CircularProgressIndicator()
        if (networkState is NetworkResult.Error) {
            Text("Мережа недоступна — показано останній кешований результат")
        }

        val critical = viewModel.filterCritical(localReadings)
        Text("Усього в кеші: ${localReadings.size}, поза нормою: ${critical.size}")
        LazyColumn {
            items(localReadings) { state ->
                val status = ProcessStatus.fromMethane(state.methaneConcentration)
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                    Text("CH4: ${state.methaneConcentration}%  ")
                    Text(status.label(), color = statusLabelColor(
                        isCritical = status == ProcessStatus.CRITICAL,
                        isWarning = status == ProcessStatus.WARNING
                    ))
                }
            }
        }
    }
}

@Composable
fun Lesson10Advanced(viewModel: BiogasViewModel = viewModel()) {
    val networkState by viewModel.networkState.collectAsState()
    val average by viewModel.averageMethane.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Просунутий рівень: агрегація + адаптивні картки")
        MenuButton("Синхронізувати з повторними спробами") {
            viewModel.refreshWithRetryAndAggregate()
        }

        when (val state = networkState) {
            is NetworkResult.Loading -> CircularProgressIndicator()
            is NetworkResult.Error -> Text("Не вдалося завантажити дані: ${state.message}")
            is NetworkResult.Success -> {
                average?.let { Text("Середній CH4 по всіх реакторах: ${"%.1f".format(it)}%") }
                LazyColumn {
                    items(state.data) { reading ->
                        val status = ProcessStatus.fromMethane(reading.methaneConcentration)
                        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                InfoRow("Наповнення", "${reading.fillLevel}%")
                                InfoRow(
                                    "Метан", "${reading.methaneConcentration}% (${status.label()})",
                                    valueColor = statusLabelColor(
                                        isCritical = status == ProcessStatus.CRITICAL,
                                        isWarning = status == ProcessStatus.WARNING
                                    )
                                )
                                InfoRow("Температура", "${reading.temperature}°C")
                            }
                        }
                    }
                }
            }
            null -> Text("Натисніть кнопку для синхронізації")
        }
    }
}
