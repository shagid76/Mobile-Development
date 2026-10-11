package com.example.practicalwork2_variant6.composables.lesson6

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practicalwork2_variant6.composables.common.InfoRow
import com.example.practicalwork2_variant6.composables.common.ReactorInputField
import com.example.practicalwork2_variant6.composables.common.ScreenTitle
import com.example.practicalwork2_variant6.composables.common.statusLabelColor
import com.example.practicalwork2_variant6.domain.BiogasCalculator
import com.example.practicalwork2_variant6.domain.ProcessStatus
import com.example.practicalwork2_variant6.domain.ReactorState
import com.example.practicalwork2_variant6.domain.ReactorValidator
import com.example.practicalwork2_variant6.domain.label

@Composable
fun BasicFormApp() {
    var fillLevelInput by remember { mutableStateOf("") }
    var methaneInput by remember { mutableStateOf("") }
    var fillLevelError by remember { mutableStateOf<String?>(null) }
    var methaneError by remember { mutableStateOf<String?>(null) }
    var resultMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Параметри реактора (базовий рівень)")

        ReactorInputField(
            label = "Рівень наповнення, %",
            value = fillLevelInput,
            onValueChange = { fillLevelInput = it },
            hint = "Введіть число від 0 до 100",
            errorMessage = fillLevelError
        )
        ReactorInputField(
            label = "Концентрація метану, %",
            value = methaneInput,
            onValueChange = { methaneInput = it },
            hint = "Введіть число від 0 до 100",
            errorMessage = methaneError
        )

        Button(
            onClick = {
                fillLevelError = ReactorValidator.validateRequiredInt("Рівень наповнення", fillLevelInput)
                methaneError = ReactorValidator.validateRequiredInt("Концентрація метану", methaneInput)
                resultMessage = if (fillLevelError == null && methaneError == null) {
                    "Дані коректні: наповнення ${fillLevelInput}%, метан ${methaneInput}%"
                } else null
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Перевірити")
        }

        resultMessage?.let { Text(it, modifier = Modifier.padding(top = 12.dp)) }
    }
}

@Composable
fun MediumFormApp() {
    var fillLevelInput by remember { mutableStateOf("") }
    var methaneInput by remember { mutableStateOf("") }
    var temperatureInput by remember { mutableStateOf("") }
    var pressureInput by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var submittedState by remember { mutableStateOf<ReactorState?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Параметри реактора (середній рівень)")

        ReactorInputField(
            label = "Рівень наповнення, %",
            value = fillLevelInput,
            onValueChange = { fillLevelInput = it },
            hint = "Допустимий діапазон: 0–100%",
            errorMessage = errors["fillLevel"]
        )
        ReactorInputField(
            label = "Концентрація метану, %",
            value = methaneInput,
            onValueChange = { methaneInput = it },
            hint = "Допустимий діапазон: 0–100%",
            errorMessage = errors["methane"]
        )
        ReactorInputField(
            label = "Температура, °C",
            value = temperatureInput,
            onValueChange = { temperatureInput = it },
            hint = "Допустимий діапазон: -10…80 °C",
            errorMessage = errors["temperature"]
        )
        ReactorInputField(
            label = "Тиск, кПа",
            value = pressureInput,
            onValueChange = { pressureInput = it },
            hint = "Допустимий діапазон: 0–500 кПа",
            errorMessage = errors["pressure"]
        )

        Button(
            onClick = {
                val validationErrors = ReactorValidator.validateReactorForm(
                    fillLevelInput, methaneInput, temperatureInput, pressureInput
                )
                errors = validationErrors
                submittedState = if (validationErrors.isEmpty()) {
                    ReactorState(
                        fillLevel = fillLevelInput.toInt(),
                        methaneConcentration = methaneInput.toInt(),
                        temperature = temperatureInput.toInt(),
                        pressure = pressureInput.toInt()
                    )
                } else null
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Перевірити та зберегти")
        }

        submittedState?.let { state ->
            val status = ProcessStatus.fromMethane(state.methaneConcentration)
            Column(modifier = Modifier.padding(top = 12.dp)) {
                InfoRow("Статус процесу", status.label(), statusLabelColor(status == ProcessStatus.CRITICAL, status == ProcessStatus.WARNING))
            }
        }
    }
}

@Composable
fun AdvancedFormApp() {
    var fillLevelInput by remember { mutableStateOf("") }
    var methaneInput by remember { mutableStateOf("") }
    var temperatureInput by remember { mutableStateOf("") }
    var pressureInput by remember { mutableStateOf("") }

    val errors = ReactorValidator.validateReactorForm(
        fillLevelInput, methaneInput, temperatureInput, pressureInput
    )
    val crossWarning = if (errors.isEmpty()) {
        ReactorValidator.crossFieldWarning(fillLevelInput.toInt(), methaneInput.toInt())
    } else null

    val calculator = remember { BiogasCalculator() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        ScreenTitle("Параметри реактора (просунутий рівень)")

        ReactorInputField(
            label = "Рівень наповнення, %",
            value = fillLevelInput,
            onValueChange = { fillLevelInput = it },
            hint = "Допустимий діапазон: 0–100%",
            errorMessage = errors["fillLevel"]
        )
        ReactorInputField(
            label = "Концентрація метану, %",
            value = methaneInput,
            onValueChange = { methaneInput = it },
            hint = "Допустимий діапазон: 0–100%",
            errorMessage = errors["methane"]
        )
        ReactorInputField(
            label = "Температура, °C",
            value = temperatureInput,
            onValueChange = { temperatureInput = it },
            hint = "Допустимий діапазон: -10…80 °C",
            errorMessage = errors["temperature"]
        )
        ReactorInputField(
            label = "Тиск, кПа",
            value = pressureInput,
            onValueChange = { pressureInput = it },
            hint = "Допустимий діапазон: 0–500 кПа",
            errorMessage = errors["pressure"]
        )

        crossWarning?.let {
            Text(it, color = androidx.compose.material3.MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        if (errors.isEmpty()) {
            val state = ReactorState(
                fillLevel = fillLevelInput.toInt(),
                methaneConcentration = methaneInput.toInt(),
                temperature = temperatureInput.toInt(),
                pressure = pressureInput.toInt()
            )
            val status = ProcessStatus.fromMethane(state.methaneConcentration)
            val dailyYield = calculator.estimateDailyYield(state)

            Column(modifier = Modifier.padding(top = 16.dp)) {
                ScreenTitle("Результат")
                InfoRow("Статус процесу", status.label(), statusLabelColor(status == ProcessStatus.CRITICAL, status == ProcessStatus.WARNING))
                InfoRow("Орієнтовний вихід біогазу", "%.2f м³/добу".format(dailyYield))
            }
        }
    }
}
