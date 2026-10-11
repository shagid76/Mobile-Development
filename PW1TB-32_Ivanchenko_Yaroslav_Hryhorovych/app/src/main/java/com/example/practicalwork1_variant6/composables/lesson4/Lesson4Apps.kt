package com.example.practicalwork1_variant6.composables.lesson4

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.practicalwork1_variant6.domain.AuthValidator
import kotlinx.coroutines.delay

@Composable
fun BasicAuthApp() {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Авторизація для Біогазової установки", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Логін") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Пароль") })
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            message = AuthValidator.validateLogin(login, password) ?: "Увійти"
        }) {
            Text("Увійти")
        }

        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(message, color = Color.Red)
        }
    }
}

@Composable
fun MediumAuthApp() {
    var isRegisterMode by remember { mutableStateOf(false) }

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isRegisterMode) "Реєстрація" else "Вхід",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isRegisterMode) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Ім'я") })
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Електронна пошта") })
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = regPassword, onValueChange = { regPassword = it }, label = { Text("Пароль") })
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Підтвердження пароля") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                message = AuthValidator.validateRegistration(name, email, regPassword, confirmPassword)
                    ?: "Реєстрація успішна"
            }) {
                Text("Зареєструватися")
            }
        } else {
            OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Логін") })
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Пароль") })
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                message = AuthValidator.validateLogin(login, password) ?: "Увійти"
            }) {
                Text("Увійти")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {
            isRegisterMode = !isRegisterMode
            message = ""
        }) {
            Text(if (isRegisterMode) "Вже маєте акаунт? Увійти" else "Немає акаунту? Зареєструватися")
        }

        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(message)
        }
    }
}

@Composable
fun AdvancedAuthApp() {
    var isRegisterMode by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var triggerSubmit by remember { mutableStateOf(0) }

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthHeader(isRegisterMode)
        Spacer(modifier = Modifier.height(16.dp))

        if (isRegisterMode) {
            RegisterFields(
                name = name, onNameChange = { name = it },
                email = email, onEmailChange = { email = it },
                password = regPassword, onPasswordChange = { regPassword = it },
                confirmPassword = confirmPassword, onConfirmPasswordChange = { confirmPassword = it }
            )
        } else {
            LoginFields(
                login = login, onLoginChange = { login = it },
                password = password, onPasswordChange = { password = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                message = if (isRegisterMode) {
                    AuthValidator.validateRegistration(name, email, regPassword, confirmPassword) ?: ""
                } else {
                    AuthValidator.validateLogin(login, password) ?: ""
                }
                if (message.isEmpty()) {
                    isLoading = true
                    triggerSubmit++
                }
            }) {
                Text(if (isRegisterMode) "Зареєструватися" else "Увійти")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {
            isRegisterMode = !isRegisterMode
            message = ""
        }) {
            Text(if (isRegisterMode) "Вже маєте акаунт? Увійти" else "Немає акаунту? Зареєструватися")
        }

        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(message, color = Color.Red)
        }
    }

    LaunchedEffect(triggerSubmit) {
        if (triggerSubmit > 0) {
            delay(1200)
            isLoading = false
            message = if (isRegisterMode) "Реєстрація успішна" else "Вхід виконано успішно"
        }
    }
}

@Composable
private fun AuthHeader(isRegisterMode: Boolean) {
    Text(
        text = if (isRegisterMode) "Реєстрація в системі моніторингу" else "Вхід у систему моніторингу",
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun LoginFields(
    login: String,
    onLoginChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit
) {
    OutlinedTextField(value = login, onValueChange = onLoginChange, label = { Text("Логін") })
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(value = password, onValueChange = onPasswordChange, label = { Text("Пароль") })
}

@Composable
private fun RegisterFields(
    name: String, onNameChange: (String) -> Unit,
    email: String, onEmailChange: (String) -> Unit,
    password: String, onPasswordChange: (String) -> Unit,
    confirmPassword: String, onConfirmPasswordChange: (String) -> Unit
) {
    OutlinedTextField(value = name, onValueChange = onNameChange, label = { Text("Ім'я") })
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(value = email, onValueChange = onEmailChange, label = { Text("Електронна пошта") })
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(value = password, onValueChange = onPasswordChange, label = { Text("Пароль") })
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(value = confirmPassword, onValueChange = onConfirmPasswordChange, label = { Text("Підтвердження пароля") })
}
