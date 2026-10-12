package com.example.practicalwork3_variant6.composables.lesson12

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicalwork3_variant6.composables.common.MenuButton
import com.example.practicalwork3_variant6.composables.common.ScreenTitle
import com.example.practicalwork3_variant6.data.remote.NetworkResult
import com.example.practicalwork3_variant6.viewmodel.ForecastViewModel

/**
 * Заняття №12, базовий рівень: перевірка наявності інтернет-з'єднання
 * перед виконанням запиту і показ простого повідомлення про помилку.
 */
@Composable
fun Lesson12Basic(viewModel: ForecastViewModel = viewModel()) {
    val isConnected by viewModel.isConnected.collectAsState()
    val result by viewModel.result.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Базовий рівень: перевірка з'єднання")
        Text(if (isConnected) "Мережа доступна" else "Мережа недоступна", color =
            if (isConnected) Color(0xFF2D6A4F) else MaterialErrorColor())
        MenuButton("Перевірити з'єднання") { viewModel.refreshConnectivity() }
        MenuButton("Отримати прогноз") { viewModel.fetchBasic() }

        when (val state = result) {
            is NetworkResult.Loading -> CircularProgressIndicator()
            is NetworkResult.Error -> Text("⚠ ${state.message}", color = MaterialErrorColor())
            is NetworkResult.Success -> Text("Прогноз CH4: ${state.data.forecastMethane}%")
            null -> {}
        }
    }
}

/**
 * Заняття №12, середній рівень: централізований обробник помилок
 * ([com.example.practicalwork3_variant6.data.remote.safeApiCall]) логує помилки у Logcat
 * і передає готове повідомлення у ViewModel для показу через текстовий блок (аналог Snackbar).
 */
@Composable
fun Lesson12Medium(viewModel: ForecastViewModel = viewModel()) {
    val result by viewModel.result.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Середній рівень: централізований обробник помилок")
        MenuButton("Отримати прогноз (нестабільний сервер)") { viewModel.fetchMedium() }

        when (val state = result) {
            is NetworkResult.Loading -> CircularProgressIndicator()
            is NetworkResult.Error -> SnackbarLikeMessage("⚠ ${state.message}")
            is NetworkResult.Success -> Text("Прогноз CH4: ${state.data.forecastMethane}% (${state.data.note})")
            null -> Text("Натисніть кнопку — сервер випадково повертає помилку чи успіх")
        }
    }
}

/**
 * Заняття №12, просунутий рівень: автоматичні повторні спроби (retry) при
 * тимчасових помилках, різні повідомлення залежно від HTTP-коду, та
 * реєстрація помилок у файл-журнал ([ForecastViewModel]).
 */
@Composable
fun Lesson12Advanced(viewModel: ForecastViewModel = viewModel()) {
    val result by viewModel.result.collectAsState()
    val attempts by viewModel.attemptsLog.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Просунутий рівень: retry-логіка та журнал помилок")
        MenuButton("Отримати прогноз (з повторними спробами)") { viewModel.fetchAdvanced() }

        Text("Журнал спроб:")
        LazyColumn {
            items(attempts) { entry -> Text(entry) }
        }

        when (val state = result) {
            is NetworkResult.Loading -> CircularProgressIndicator()
            is NetworkResult.Error -> SnackbarLikeMessage(
                "⚠ Усі спроби невдалі (код ${state.code ?: "—"}): ${state.message}"
            )
            is NetworkResult.Success -> Text("Прогноз CH4: ${state.data.forecastMethane}%")
            null -> {}
        }
    }
}

@Composable
private fun SnackbarLikeMessage(text: String) {
    // Спрощена імітація Snackbar прямо в розмітці екрана — виокремлена
    // в окрему функцію, щоб підкреслити, що повідомлення про помилку
    // формується централізовано, а не в кожному екрані окремо.
    Text(text, color = MaterialErrorColor())
}

@Composable
private fun MaterialErrorColor() = androidx.compose.material3.MaterialTheme.colorScheme.error
