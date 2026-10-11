package com.example.practicalwork1_variant6.composables.lesson1

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
import kotlin.random.Random

@Composable
fun BiogasAppLevel2() {
    var fillLevel by remember { mutableIntStateOf(0) }
    var methane by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E1))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Біогазова установка", fontSize = 24.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(text = "Наповнення: $fillLevel%", fontSize = 18.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Метан: $methane%", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            if (fillLevel <= 99) fillLevel += Random.nextInt(0, 101 - fillLevel)
            if (methane <= 99) methane += Random.nextInt(0, 101 - methane)
        }) {
            Text("Оновити показники")
        }
    }
}
