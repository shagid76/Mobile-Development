package com.example.practicalwork6_variant6.composables.lesson22

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.practicalwork6_variant6.composables.common.SimpleLineChart
import com.example.practicalwork6_variant6.domain.ProcessCodec
import com.example.practicalwork6_variant6.domain.ProcessRecord

@Composable
fun Lesson22Basic() {
    val context = LocalContext.current
    val storage = remember { ProcessFileStorage(context) }

    var date by remember { mutableStateOf("2025-09-17") }
    var output by remember { mutableStateOf("") }
    var records by remember { mutableStateOf(listOf<ProcessRecord>()) }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Базовий рівень: CSV-файл процесу", style = MaterialTheme.typography.titleMedium)
        Text("Введіть дані про вихід біогазу", style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(
            value = date, onValueChange = { date = it },
            label = { Text("Дата (наприклад, 2025-09-17)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        OutlinedTextField(
            value = output, onValueChange = { output = it },
            label = { Text("Вихід газу (м³)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Button(onClick = {
                val value = output.toDoubleOrNull()
                if (date.isBlank() || value == null) {
                    message = "Некоректні дані: перевірте дату та число"
                } else {
                    records = records + ProcessRecord(date, value)
                    storage.saveCsv(records)
                        .onSuccess { message = "Збережено у CSV" }
                        .onFailure { message = "Помилка запису: ${it.message}" }
                }
            }) { Text("Зберегти у CSV") }

            OutlinedButton(onClick = {
                storage.loadCsv()
                    .onSuccess { records = it; message = "Завантажено ${it.size} записів" }
                    .onFailure { message = "Помилка читання: ${it.message}" }
            }, modifier = Modifier.padding(start = 8.dp)) { Text("Завантажити з CSV") }
        }
        OutlinedButton(
            onClick = {
                if (storage.deleteCsv()) { records = emptyList(); message = "CSV-файл видалено" }
                else message = "Файл відсутній"
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) { Text("Видалити файл CSV") }

        Text(message, style = MaterialTheme.typography.labelSmall)
        Text("Дані з файлу:", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
        LazyColumn {
            items(records) { r ->
                Text("Дата: ${r.date}, Вихід: ${r.output} м³")
            }
        }
    }
}

private val STATUSES = listOf("Активна", "Поломка", "Обслуговування")

@Composable
fun Lesson22Medium() {
    val context = LocalContext.current
    val storage = remember { ProcessFileStorage(context) }

    var date by remember { mutableStateOf("2025-09-17") }
    var output by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(STATUSES[0]) }
    var statusMenuExpanded by remember { mutableStateOf(false) }
    var records by remember { mutableStateOf(listOf<ProcessRecord>()) }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Середній рівень: JSON-файл процесу", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = date, onValueChange = { date = it },
            label = { Text("Дата (наприклад, 2025-09-17)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        OutlinedTextField(
            value = output, onValueChange = { output = it },
            label = { Text("Вихід газу (м³)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        Column(modifier = Modifier.padding(top = 8.dp)) {
            OutlinedButton(onClick = { statusMenuExpanded = true }) { Text("Статус: $status") }
            DropdownMenu(expanded = statusMenuExpanded, onDismissRequest = { statusMenuExpanded = false }) {
                STATUSES.forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = { status = it; statusMenuExpanded = false })
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Button(onClick = {
                val value = output.toDoubleOrNull()
                if (date.isBlank() || value == null) {
                    message = "Некоректні дані"
                } else {
                    records = records + ProcessRecord(date, value, status)
                    storage.saveJson(records)
                        .onSuccess { message = "Збережено у JSON" }
                        .onFailure { message = "Помилка запису: ${it.message}" }
                }
            }) { Text("Зберегти у JSON") }

            OutlinedButton(onClick = {
                storage.loadJson()
                    .onSuccess { records = it; message = "Завантажено ${it.size} записів" }
                    .onFailure { message = "Помилка читання: ${it.message}" }
            }, modifier = Modifier.padding(start = 8.dp)) { Text("Завантажити з JSON") }
        }
        OutlinedButton(
            onClick = {
                if (storage.deleteJson()) { records = emptyList(); message = "JSON-файл видалено" }
                else message = "Файл відсутній"
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) { Text("Видалити файл JSON") }

        Text(message, style = MaterialTheme.typography.labelSmall)
        Text("Дані з файлу (JSON):", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
        LazyColumn {
            items(records) { r ->
                Text("Дата: ${r.date}, Вихід: ${r.output} м³, Статус: ${r.status}")
            }
        }
    }
}

@Composable
fun Lesson22Advanced() {
    val context = LocalContext.current
    val storage = remember { ProcessFileStorage(context) }

    var from by remember { mutableStateOf("2000-01-01") }
    var to by remember { mutableStateOf("2099-12-31") }
    var filtered by remember { mutableStateOf(listOf<ProcessRecord>()) }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Просунутий рівень: аналітика (графік)", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = from, onValueChange = { from = it },
            label = { Text("Дата від (yyyy-MM-dd)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        OutlinedTextField(
            value = to, onValueChange = { to = it },
            label = { Text("Дата до (yyyy-MM-dd)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Button(onClick = {
                val csv = storage.loadCsv().getOrDefault(emptyList())
                val json = storage.loadJson().getOrDefault(emptyList())
                val merged = ProcessCodec.sortedByDate(csv + json)
                filtered = ProcessCodec.filterByDateRange(merged, from, to)
                message = "Знайдено ${filtered.size} записів"
            }) { Text("Завантажити CSV/JSON") }

            OutlinedButton(onClick = {
                val csvDeleted = storage.deleteCsv()
                val jsonDeleted = storage.deleteJson()
                filtered = emptyList()
                message = if (csvDeleted || jsonDeleted) "Файли видалено" else "Файли відсутні"
            }, modifier = Modifier.padding(start = 8.dp)) { Text("Видалити файли") }
        }
        Text(message, style = MaterialTheme.typography.labelSmall)
        if (filtered.isNotEmpty()) {
            SimpleLineChart(values = filtered.map { it.output.toFloat() })
        }
        LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
            items(filtered) { r ->
                Text("${r.date}: ${r.output} м³" + if (r.status.isNotBlank()) ", ${r.status}" else "")
            }
        }
    }
}
