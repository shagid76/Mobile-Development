package com.example.practicalwork5_variant6.composables.lesson18

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun Lesson18Basic() {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text("Базовий рівень: просте локальне сповіщення", style = MaterialTheme.typography.titleMedium)
        Button(onClick = { sendBasicNotification(context) }, modifier = Modifier.fillMaxWidth()) {
            Text("Надіслати сповіщення")
        }
    }
}

@Composable
fun Lesson18Medium() {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text("Середній рівень: категоризовані сповіщення", style = MaterialTheme.typography.titleMedium)
        Button(
            onClick = {
                sendCategorizedNotification(
                    context, "Температура реактора",
                    "Температура перевищила 42°C", NotificationIds.TEMPERATURE_NOTIFICATION_ID
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Нагадування про температуру") }

        Spacer()

        Button(
            onClick = {
                sendCategorizedNotification(
                    context, "Тиск біогазу",
                    "Тиск газгольдера перевищив 5 кПа", NotificationIds.PRESSURE_NOTIFICATION_ID
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Нагадування про тиск") }
    }
}

@Composable
fun Lesson18Advanced() {
    val context = LocalContext.current
    val lastAction by NotificationActionState.lastAction.collectAsState()

    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text("Просунутий рівень: інтерактивні сповіщення", style = MaterialTheme.typography.titleMedium)
        Button(
            onClick = { sendInteractiveNotification(context) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Надіслати інтерактивне сповіщення") }

        Spacer()

        Text(
            text = lastAction ?: "Дія ще не виконана",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun Spacer() {
    androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
}
