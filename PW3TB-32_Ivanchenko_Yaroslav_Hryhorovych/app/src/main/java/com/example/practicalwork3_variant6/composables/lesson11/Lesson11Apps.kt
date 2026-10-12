package com.example.practicalwork3_variant6.composables.lesson11

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.example.practicalwork3_variant6.composables.common.MenuButton
import com.example.practicalwork3_variant6.composables.common.ReactorInputField
import com.example.practicalwork3_variant6.composables.common.ScreenTitle
import com.example.practicalwork3_variant6.data.remote.NetworkResult
import com.example.practicalwork3_variant6.viewmodel.AuthViewModel

@Composable
fun Lesson11Basic(viewModel: AuthViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()
    val role by viewModel.role.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Базовий рівень: мокований вхід і токен")
        if (role != null) {
            Text("Ви увійшли як: $role")
            MenuButton("Вийти") { viewModel.logout() }
        } else {
            ReactorInputField("Логін", username, { username = it }, "наприклад, operator", null)
            ReactorInputField("Пароль", password, { password = it }, "наприклад, operator123", null)
            MenuButton("Увійти") { viewModel.login(username, password) }
            when (val state = loginState) {
                is NetworkResult.Loading -> CircularProgressIndicator()
                is NetworkResult.Error -> Text("Помилка: ${state.message}")
                is NetworkResult.Success -> Text("Успішний вхід, роль: ${state.data}")
                null -> {}
            }
        }
    }
}

@Composable
fun Lesson11Medium(viewModel: AuthViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val role by viewModel.role.collectAsState()
    val secureStatus by viewModel.secureStatus.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Середній рівень: захищений ресурс через Interceptor")
        if (role == null) {
            ReactorInputField("Логін", username, { username = it }, "admin або operator", null)
            ReactorInputField("Пароль", password, { password = it }, "", null)
            MenuButton("Увійти") { viewModel.login(username, password) }
        } else {
            Text("Роль: $role")
            MenuButton("Отримати захищені дані") { viewModel.loadSecureStatus() }
            MenuButton("Вийти") { viewModel.logout() }
            when (val state = secureStatus) {
                is NetworkResult.Loading -> CircularProgressIndicator()
                is NetworkResult.Error -> Text(
                    if (state.code == 401) "Токен недійсний або прострочений — увійдіть знову"
                    else "Помилка: ${state.message}"
                )
                is NetworkResult.Success -> Text(
                    "Реакторів онлайн: ${state.data.reactorsOnline}, " +
                        "середній CH4: ${"%.1f".format(state.data.averageMethane)}%"
                )
                null -> {}
            }
        }
    }
}

@Composable
fun Lesson11Advanced(viewModel: AuthViewModel = viewModel()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val role by viewModel.role.collectAsState()
    val secureStatus by viewModel.secureStatus.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTitle("Просунутий рівень: розмежування доступу за роллю")
        if (role == null) {
            ReactorInputField("Логін", username, { username = it }, "admin / operator", null)
            ReactorInputField("Пароль", password, { password = it }, "admin123 / operator123", null)
            MenuButton("Увійти") { viewModel.login(username, password) }
            MenuButton("Спробувати доступ без входу") { viewModel.loadSecureStatus() }
        } else {
            Text("Вхід виконано, роль: $role")
            MenuButton("Оновити захищені дані") { viewModel.loadSecureStatus() }
            MenuButton("Вийти") { viewModel.logout() }
        }

        when (val state = secureStatus) {
            is NetworkResult.Loading -> CircularProgressIndicator()
            is NetworkResult.Error -> Text("Доступ заборонено: ${state.message}")
            is NetworkResult.Success -> {
                Text("Реакторів онлайн: ${state.data.reactorsOnline}")
                Text("Середній CH4: ${"%.1f".format(state.data.averageMethane)}%")
                // Адмін-панель показується лише якщо роль у токені — ADMIN.
                if (state.data.role == "ADMIN" && state.data.adminNote != null) {
                    Text("⚙ Адмін-панель: ${state.data.adminNote}")
                }
            }
            null -> {}
        }
    }
}
