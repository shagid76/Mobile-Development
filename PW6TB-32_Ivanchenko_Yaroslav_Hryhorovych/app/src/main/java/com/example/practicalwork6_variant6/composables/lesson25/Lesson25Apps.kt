package com.example.practicalwork6_variant6.composables.lesson25

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practicalwork6_variant6.domain.ProcessCodec
import com.example.practicalwork6_variant6.domain.ProcessRecord
import com.example.practicalwork6_variant6.domain.ResourceEvaluator
import com.example.practicalwork6_variant6.domain.ResourceStatus

private data class CheckResult(val name: String, val passed: Boolean, val detail: String)

private fun runBasicChecks(): List<CheckResult> = listOf(
    run {
        val record = ProcessCodec.parseCsvLine("2025-09-10,48.0")
        CheckResult(
            "parseCsvLine коректний рядок",
            record != null && record.date == "2025-09-10",
            "Очікувано: дата 2025-09-10"
        )
    },
    run {
        val record = ProcessCodec.parseCsvLine(",не_число")
        CheckResult("parseCsvLine відхиляє некоректний рядок", record == null, "Очікувано: null")
    },
    run {
        val status = ResourceEvaluator.classify(95f)
        CheckResult("classify(95%) = CRITICAL", status == ResourceStatus.CRITICAL, "Отримано: $status")
    }
)

@Composable
fun Lesson25Basic() {
    val results = remember { runBasicChecks() }
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Базовий рівень: модульні (unit) перевірки", style = MaterialTheme.typography.titleMedium)
        Text(
            "Повні JUnit-тести: app/src/test/.../ProcessCodecTest.kt, ResourceEvaluatorTest.kt",
            style = MaterialTheme.typography.labelSmall
        )
        CheckResultList(results)
    }
}

@Composable
fun Lesson25Medium() {
    var csvInput by remember { mutableStateOf("2025-09-10,48.0") }
    var mockedResult by remember { mutableStateOf<CheckResult?>(null) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Середній рівень: UI-сценарій з mocked-даними", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = csvInput, onValueChange = { csvInput = it },
            label = { Text("Тестовий CSV-рядок (mocked data)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        Button(
            onClick = {
                val parsed = ProcessCodec.parseCsvLine(csvInput)
                mockedResult = CheckResult(
                    name = "Парсинг введеного рядка",
                    passed = parsed != null,
                    detail = parsed?.let { "Дата ${it.date}, вихід ${it.output}" } ?: "Рядок відхилено як некоректний"
                )
            },
            modifier = Modifier.padding(top = 8.dp)
        ) { Text("Запустити сценарій") }

        mockedResult?.let { CheckResultList(listOf(it)) }
    }
}

private fun runAdvancedScenarios(): List<CheckResult> {
    val checks = mutableListOf<CheckResult>()

    checks += runBasicChecks()

    val original = listOf(ProcessRecord("2025-09-10", 48.0, "Активна"))
    val roundTrip = ProcessCodec.decodeJson(ProcessCodec.encodeJson(original))
    checks += CheckResult(
        "Інтеграція CSV→JSON→модель",
        roundTrip == original,
        "Round-trip ${if (roundTrip == original) "збігається" else "НЕ збігається"}"
    )

    checks += CheckResult(
        "Граничний випадок: 0% пам'яті",
        ResourceEvaluator.memoryUsagePercent(0L, 0L) == 0f,
        "Очікувано 0.0 без ділення на нуль"
    )

    return checks
}

@Composable
fun Lesson25Advanced() {
    val results = remember { runAdvancedScenarios() }
    val passed = results.count { it.passed }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Просунутий рівень: звіт про тестування", style = MaterialTheme.typography.titleMedium)
        Text(
            "Пройдено $passed з ${results.size} перевірок (юніт + інтеграційні + граничні випадки)",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            "UI-тести виконуються окремо: ./gradlew connectedDebugAndroidTest",
            style = MaterialTheme.typography.labelSmall
        )
        CheckResultList(results)
    }
}

@Composable
private fun CheckResultList(results: List<CheckResult>) {
    LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
        items(results) { r ->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text(if (r.passed) "✅" else "❌", modifier = Modifier.padding(end = 8.dp))
                Column {
                    Text(r.name, style = MaterialTheme.typography.bodyMedium)
                    Text(r.detail, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
