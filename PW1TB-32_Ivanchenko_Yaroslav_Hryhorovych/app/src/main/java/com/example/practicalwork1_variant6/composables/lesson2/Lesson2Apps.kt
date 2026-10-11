package com.example.practicalwork1_variant6.composables.lesson2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practicalwork1_variant6.domain.BiogasCalculator

@Composable
fun BasicBiogasApp() {
    var massInput by remember { mutableStateOf("") }
    var result by remember { mutableStateOf(0.0) }
    val calculator = remember { BiogasCalculator() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Базовий рівень: введіть масу сировини", style = MaterialTheme.typography.titleMedium)

        TextField(
            value = massInput,
            onValueChange = { massInput = it },
            label = { Text("Маса сировини (кг)") }
        )

        Button(onClick = { result = calculator.calculateBiogasBasic(massInput) }) {
            Text("Розрахувати")
        }

        Text("Розрахований вихід біогазу: $result м3", style = MaterialTheme.typography.bodyLarge)
    }
}


@Composable
fun MediumBiogasApp() {
    var massInput by remember { mutableStateOf("") }
    var humidityInput by remember { mutableStateOf("") }
    var result by remember { mutableStateOf(0.0) }
    val calculator = remember { BiogasCalculator() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Середній рівень: декілька параметрів", style = MaterialTheme.typography.titleMedium)

        TextField(
            value = massInput,
            onValueChange = { massInput = it },
            label = { Text("Маса сировини (кг)") }
        )
        TextField(
            value = humidityInput,
            onValueChange = { humidityInput = it },
            label = { Text("Вологість сировини (%)") }
        )

        Button(onClick = { result = calculator.calculateBiogasMedium(massInput, humidityInput) }) {
            Text("Розрахувати")
        }

        Text("Розрахований вихід біогазу: $result м3", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun AdvancedBiogasApp() {
    var massInput by remember { mutableStateOf("") }
    var humidityInput by remember { mutableStateOf("") }
    var retentionInput by remember { mutableStateOf("") }
    var biogasResult by remember { mutableStateOf(0.0) }
    var methaneResult by remember { mutableStateOf(0.0) }
    val calculator = remember { BiogasCalculator() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Просунутий рівень: масштабування проєкту", style = MaterialTheme.typography.titleMedium)

        TextField(
            value = massInput,
            onValueChange = { massInput = it },
            label = { Text("Маса сировини (кг)") }
        )
        TextField(
            value = humidityInput,
            onValueChange = { humidityInput = it },
            label = { Text("Вологість сировини (%)") }
        )
        TextField(
            value = retentionInput,
            onValueChange = { retentionInput = it },
            label = { Text("Час утримання в реакторі (діб)") }
        )

        Button(onClick = {
            val (biogas, methane) = calculator.calculateBiogasAdvanced(
                massInput, humidityInput, retentionInput
            )
            biogasResult = biogas
            methaneResult = methane
        }) {
            Text("Розрахувати")
        }

        Text("Розрахований вихід біогазу: $biogasResult м3", style = MaterialTheme.typography.bodyLarge)
        Text("Орієнтовний вихід метану: $methaneResult м3", style = MaterialTheme.typography.bodyLarge)
    }
}
