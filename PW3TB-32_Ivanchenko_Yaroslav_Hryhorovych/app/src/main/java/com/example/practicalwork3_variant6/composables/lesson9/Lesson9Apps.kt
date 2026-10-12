package com.example.practicalwork3_variant6.composables.lesson9

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicalwork3_variant6.composables.common.InfoRow
import com.example.practicalwork3_variant6.composables.common.MenuButton
import com.example.practicalwork3_variant6.composables.common.ReactorInputField
import com.example.practicalwork3_variant6.composables.common.ScreenTitle
import com.example.practicalwork3_variant6.data.remote.NetworkResult
import com.example.practicalwork3_variant6.domain.ReactorState
import com.example.practicalwork3_variant6.viewmodel.BiogasViewModel

/**
 * Заняття №9, базовий рівень: одне локальне джерело даних (Room) +
 * Repository з базовими методами вставки/отримання. Мережа не задіяна.
 */
@Composable
fun Lesson9Basic(viewModel: BiogasViewModel = viewModel()) {
    val readings by viewModel.localReadings.collectAsState()
    var methane by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Базовий рівень: локальний Repository (Room)")
        ReactorInputField(
            label = "Концентрація метану, %",
            value = methane,
            onValueChange = { methane = it },
            hint = "Введіть число від 0 до 100",
            errorMessage = null
        )
        MenuButton("Зберегти показник локально") {
            val value = methane.toIntOrNull() ?: 0
            viewModel.saveLocally(ReactorState(methaneConcentration = value))
            methane = ""
        }
        Text("Збережені показники (${readings.size}):")
        LazyColumn {
            items(readings) { state ->
                InfoRow("Метан", "${state.methaneConcentration}%")
            }
        }
    }
}

/**
 * Заняття №9, середній рівень: Repository об'єднує локальне (Room) і
 * віддалене (REST) джерела — "Завантажити з сервера" синхронізує обидва.
 */
@Composable
fun Lesson9Medium(viewModel: BiogasViewModel = viewModel()) {
    val localReadings by viewModel.localReadings.collectAsState()
    val networkState by viewModel.networkState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Середній рівень: об'єднання Local + Remote Data Source")
        MenuButton("Завантажити показники з сервера") { viewModel.refreshFromServer() }

        when (val state = networkState) {
            is NetworkResult.Loading -> CircularProgressIndicator()
            is NetworkResult.Error -> Text("Помилка: ${state.message}")
            is NetworkResult.Success -> Text("Отримано ${state.data.size} показників із сервера")
            null -> Text("Натисніть кнопку, щоб синхронізувати дані")
        }

        Text("Локальний кеш (${localReadings.size} записів):")
        LazyColumn {
            items(localReadings) { state ->
                Card(modifier = Modifier.fillMaxSize().padding(vertical = 2.dp)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        InfoRow("Наповнення", "${state.fillLevel}%")
                        InfoRow("Метан", "${state.methaneConcentration}%")
                    }
                }
            }
        }
    }
}

/**
 * Заняття №9, просунутий рівень: складна структура Repository з агрегацією
 * (середнє значення метану), сортуванням/фільтрацією та retry-логікою
 * (повторні спроби запиту делеговані репозиторію — дивись [BiogasViewModel.refreshWithRetryAndAggregate]).
 */
@Composable
fun Lesson9Advanced(viewModel: BiogasViewModel = viewModel()) {
    val localReadings by viewModel.localReadings.collectAsState()
    val networkState by viewModel.networkState.collectAsState()
    val average by viewModel.averageMethane.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Просунутий рівень: агрегація, кешування, retry")
        MenuButton("Синхронізувати (з повторними спробами)") {
            viewModel.refreshWithRetryAndAggregate()
        }

        when (val state = networkState) {
            is NetworkResult.Loading -> CircularProgressIndicator()
            is NetworkResult.Error -> Text("Помилка після всіх спроб: ${state.message}")
            is NetworkResult.Success -> Text("Синхронізовано ${state.data.size} показників")
            null -> Text("Натисніть кнопку для синхронізації")
        }
        average?.let { Text("Середня концентрація метану: ${"%.1f".format(it)}%") }

        val critical = viewModel.filterCritical(localReadings)
        Text("Показники поза нормою (${critical.size}):")
        LazyColumn {
            items(viewModel.sortByMethaneDescending(critical)) { state ->
                InfoRow("Метан", "${state.methaneConcentration}%")
            }
        }
    }
}
