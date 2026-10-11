package com.example.practicalwork1_variant6.composables.lesson1

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practicalwork1_variant6.domain.ProcessStatus
import kotlin.random.Random

@Composable
fun BiogasAppLevel3() {
    var fillLevel by remember { mutableIntStateOf(0) }
    var methane by remember { mutableIntStateOf(0) }

    val animatedFillLevel by animateIntAsState(targetValue = fillLevel, label = "fillLevel")
    val animatedMethane by animateIntAsState(targetValue = methane, label = "methane")

    val status = ProcessStatus.fromMethane(methane)
    val statusColor by animateColorAsState(
        targetValue = when (status) {
            ProcessStatus.STABLE -> Color(0xFF2D6A4F)
            ProcessStatus.WARNING -> Color(0xFFE09F3E)
            ProcessStatus.CRITICAL -> Color(0xFFBA181B)
        },
        label = "statusColor"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Біогазова установка",
            fontSize = 26.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(text = "Наповнення: $animatedFillLevel%", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Метан: $animatedMethane%", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = when (status) {
                ProcessStatus.STABLE -> "Стан процесу: стабільний"
                ProcessStatus.WARNING -> "Стан процесу: потребує уваги"
                ProcessStatus.CRITICAL -> "Стан процесу: критичний"
            },
            color = statusColor,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            if (fillLevel <= 99) fillLevel += Random.nextInt(0, 101 - fillLevel)
            if (methane <= 99) methane += Random.nextInt(0, 101 - methane)
        }) {
            Text("Оновити показники")
        }
    }
}
