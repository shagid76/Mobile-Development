package com.example.practicalwork1_variant6.composables.lesson3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BasicProcessApp() {
    var statusText by remember { mutableStateOf("Очікування запуску") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Біогазова установка", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Icon(
            imageVector = Icons.Filled.LocalFireDepartment,
            contentDescription = "Біогаз",
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(statusText)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { statusText = "Процес бродіння триває" }) {
            Text("Запустити процес")
        }
    }
}

@Composable
fun MediumProcessApp() {
    var stage by remember { mutableStateOf("Завантаження") }
    var power by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Біогазова установка — етапи процесу", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Button(onClick = { stage = "Завантаження"; power = 10 }) { Text("Завантаження") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { stage = "Бродіння"; power = 60 }) { Text("Бродіння") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { stage = "Вивантаження"; power = 20 }) { Text("Вивантаження") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Icon(
            imageVector = Icons.Filled.LocalFireDepartment,
            contentDescription = "Біогаз",
            modifier = Modifier.size(96.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Поточний етап: $stage")
        Text("Орієнтовна потужність виходу газу: $power%")
    }
}

@Composable
fun AdvancedProcessApp() {
    var stage by remember { mutableStateOf("Завантаження") }
    var power by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProcessHeader()
        Spacer(modifier = Modifier.height(12.dp))
        StageButtons(
            onStageSelected = { newStage, newPower ->
                stage = newStage
                power = newPower
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProcessInfoCard(stage = stage, power = power)
    }
}

@Composable
private fun ProcessHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.LocalFireDepartment,
            contentDescription = "Біогаз",
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Моніторинг процесу бродіння", style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun StageButtons(onStageSelected: (String, Int) -> Unit) {
    Row {
        Button(onClick = { onStageSelected("Завантаження", 10) }) { Text("Завантаження") }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onStageSelected("Бродіння", 60) }) { Text("Бродіння") }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onStageSelected("Вивантаження", 20) }) { Text("Вивантаження") }
    }
}

@Composable
private fun ProcessInfoCard(stage: String, power: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Поточний етап: $stage", style = MaterialTheme.typography.bodyLarge)
            Text("Орієнтовна потужність виходу газу: $power%", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
