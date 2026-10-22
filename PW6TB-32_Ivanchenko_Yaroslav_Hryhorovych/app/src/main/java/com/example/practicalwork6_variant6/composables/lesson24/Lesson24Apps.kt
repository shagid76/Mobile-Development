package com.example.practicalwork6_variant6.composables.lesson24

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.practicalwork6_variant6.domain.ProcessRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private fun sampleRecords(): List<ProcessRecord> = listOf(
    ProcessRecord("2025-09-10", 48.0, "Активна"),
    ProcessRecord("2025-09-11", 52.0, "Активна"),
    ProcessRecord("2025-09-12", 39.0, "Обслуговування"),
    ProcessRecord("2025-09-13", 61.0, "Активна"),
    ProcessRecord("2025-09-14", 57.0, "Активна")
)

@Composable
fun Lesson24Basic() {
    val context = LocalContext.current
    val builder = remember { PdfReportBuilder(context) }
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf("") }
    var lastFile by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Базовий рівень: PDF-звіт з таблицею", style = MaterialTheme.typography.titleMedium)
        Button(
            onClick = {
                scope.launch {
                    status = "Генерація..."
                    val result = withContext(Dispatchers.IO) {
                        builder.buildBasicReport(sampleRecords(), "Реактор-1")
                    }
                    result.onSuccess { status = "Готово: ${it.name}"; lastFile = it.name }
                        .onFailure { status = "Помилка: ${it.message}" }
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) { Text("Згенерувати PDF-звіт") }
        Text(status, style = MaterialTheme.typography.labelSmall)
        ReportList(builder)
    }
}

@Composable
fun Lesson24Medium() {
    val context = LocalContext.current
    val builder = remember { PdfReportBuilder(context) }
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf("") }
    var objectName by remember { mutableStateOf("Реактор-1") }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Середній рівень: звіт з графіком", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = objectName, onValueChange = { objectName = it },
            label = { Text("Назва об'єкта") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        Button(
            onClick = {
                scope.launch {
                    status = "Генерація..."
                    val result = withContext(Dispatchers.IO) {
                        builder.buildMediumReport(sampleRecords(), objectName)
                    }
                    result.onSuccess { status = "Готово: ${it.name}" }
                        .onFailure { status = "Помилка: ${it.message}" }
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) { Text("Згенерувати PDF з графіком") }
        Text(status, style = MaterialTheme.typography.labelSmall)
        ReportList(builder)
    }
}

@Composable
fun Lesson24Advanced() {
    val context = LocalContext.current
    val builder = remember { PdfReportBuilder(context) }
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf("") }
    var objectName by remember { mutableStateOf("Біогазова установка №1") }
    var comment by remember {
        mutableStateOf(
            "Установка працює у штатному режимі, середній вихід біогазу відповідає " +
                "плановим показникам. Рекомендовано продовжити моніторинг температури реактора."
        )
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Просунутий рівень: багатосторінковий звіт", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = objectName, onValueChange = { objectName = it },
            label = { Text("Назва об'єкта") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        OutlinedTextField(
            value = comment, onValueChange = { comment = it },
            label = { Text("Пояснення / висновки") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        Button(
            onClick = {
                scope.launch {
                    status = "Генерація багатосторінкового PDF..."
                    val result = withContext(Dispatchers.IO) {
                        builder.buildAdvancedReport(sampleRecords(), objectName, comment)
                    }
                    result.onSuccess { status = "Готово: ${it.name} (3 сторінки)" }
                        .onFailure { status = "Помилка: ${it.message}" }
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) { Text("Згенерувати повний звіт") }
        Text(status, style = MaterialTheme.typography.labelSmall)
        ReportList(builder)
    }
}

@Composable
private fun ReportList(builder: PdfReportBuilder) {
    var reports by remember { mutableStateOf(builder.listReports()) }
    Column(modifier = Modifier.padding(top = 12.dp)) {
        Text("Збережені звіти (${reports.size}):", style = MaterialTheme.typography.titleSmall)
        LazyColumn {
            items(reports) { file ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                    Text(file.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                    OutlinedButton(onClick = {
                        builder.deleteReport(file)
                        reports = builder.listReports()
                    }) { Text("Видалити") }
                }
            }
        }
    }
}
