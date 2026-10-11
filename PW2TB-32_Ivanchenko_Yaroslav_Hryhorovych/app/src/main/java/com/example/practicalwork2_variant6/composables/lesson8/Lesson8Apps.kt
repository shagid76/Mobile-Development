package com.example.practicalwork2_variant6.composables.lesson8

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practicalwork2_variant6.composables.common.InfoRow
import com.example.practicalwork2_variant6.composables.common.ReactorInputField
import com.example.practicalwork2_variant6.composables.common.ScreenTitle
import com.example.practicalwork2_variant6.composables.common.statusLabelColor
import com.example.practicalwork2_variant6.data.ReactorRecord
import com.example.practicalwork2_variant6.domain.ProcessStatus
import com.example.practicalwork2_variant6.domain.ReactorState
import com.example.practicalwork2_variant6.domain.ReactorValidator
import com.example.practicalwork2_variant6.domain.label
import com.example.practicalwork2_variant6.viewmodel.ReactorHistoryViewModel
import kotlin.random.Random

@Composable
fun BasicRoomApp(viewModel: ReactorHistoryViewModel = viewModel()) {
    val records by viewModel.records.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        ScreenTitle("Історія показників (Room)")

        Button(
            onClick = {
                viewModel.saveRecord(
                    ReactorState(
                        fillLevel = Random.nextInt(0, 101),
                        methaneConcentration = Random.nextInt(0, 101),
                        temperature = Random.nextInt(-10, 81),
                        pressure = Random.nextInt(0, 501)
                    )
                )
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) { Text("Зберегти поточні показники") }

        LazyColumn {
            items(records) { record ->
                InfoRow("Метан", "${record.methaneConcentration}%")
            }
        }
    }
}

@Composable
fun MediumRoomApp(viewModel: ReactorHistoryViewModel = viewModel()) {
    val records by viewModel.records.collectAsState()

    var fillLevelInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var methaneInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var temperatureInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var pressureInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }

    val errors = ReactorValidator.validateReactorForm(fillLevelInput, methaneInput, temperatureInput, pressureInput)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        ScreenTitle("Нові показники реактора")

        ReactorInputField("Рівень наповнення, %", fillLevelInput, { fillLevelInput = it }, "0–100%", errors["fillLevel"])
        ReactorInputField("Концентрація метану, %", methaneInput, { methaneInput = it }, "0–100%", errors["methane"])
        ReactorInputField("Температура, °C", temperatureInput, { temperatureInput = it }, "-10…80 °C", errors["temperature"])
        ReactorInputField("Тиск, кПа", pressureInput, { pressureInput = it }, "0–500 кПа", errors["pressure"])

        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Button(
                enabled = errors.isEmpty(),
                onClick = {
                    viewModel.saveRecord(
                        ReactorState(
                            fillLevel = fillLevelInput.toInt(),
                            methaneConcentration = methaneInput.toInt(),
                            temperature = temperatureInput.toInt(),
                            pressure = pressureInput.toInt()
                        )
                    )
                    fillLevelInput = ""; methaneInput = ""; temperatureInput = ""; pressureInput = ""
                },
                modifier = Modifier.weight(1f)
            ) { Text("Зберегти") }
        }

        OutlinedButton(
            onClick = { viewModel.clearAll() },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) { Text("Очистити історію") }

        Text("Історія (${records.size}):", fontWeight = FontWeight.Medium)
        LazyColumn {
            items(records) { record ->
                InfoRow("${record.methaneConcentration}% метану", "%.2f м³/добу".format(record.dailyYield))
            }
        }
    }
}

@Composable
fun AdvancedRoomApp(viewModel: ReactorHistoryViewModel = viewModel()) {
    val records by viewModel.records.collectAsState()

    var fillLevelInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var methaneInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var temperatureInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    var pressureInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }

    val errors = ReactorValidator.validateReactorForm(fillLevelInput, methaneInput, temperatureInput, pressureInput)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        ScreenTitle("Журнал показників реактора")

        ReactorInputField("Рівень наповнення, %", fillLevelInput, { fillLevelInput = it }, "0–100%", errors["fillLevel"])
        ReactorInputField("Концентрація метану, %", methaneInput, { methaneInput = it }, "0–100%", errors["methane"])
        ReactorInputField("Температура, °C", temperatureInput, { temperatureInput = it }, "-10…80 °C", errors["temperature"])
        ReactorInputField("Тиск, кПа", pressureInput, { pressureInput = it }, "0–500 кПа", errors["pressure"])

        Button(
            enabled = errors.isEmpty(),
            onClick = {
                viewModel.saveRecord(
                    ReactorState(
                        fillLevel = fillLevelInput.toInt(),
                        methaneConcentration = methaneInput.toInt(),
                        temperature = temperatureInput.toInt(),
                        pressure = pressureInput.toInt()
                    )
                )
                fillLevelInput = ""; methaneInput = ""; temperatureInput = ""; pressureInput = ""
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) { Text("Зберегти запис у базу") }

        Text("Журнал (${records.size} записів):", fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 4.dp))

        LazyColumn {
            items(records, key = { it.id }) { record -> RecordRow(record, onDelete = { viewModel.deleteRecord(record) }) }
        }
    }
}

@Composable
private fun RecordRow(record: ReactorRecord, onDelete: () -> Unit) {
    val status = ProcessStatus.fromMethane(record.methaneConcentration)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Метан ${record.methaneConcentration}% · ${"%.2f".format(record.dailyYield)} м³/добу")
            Text(
                status.label(),
                color = statusLabelColor(status == ProcessStatus.CRITICAL, status == ProcessStatus.WARNING),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Видалити запис")
        }
    }
}
