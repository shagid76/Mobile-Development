package com.example.practicalwork6_variant6.composables.lesson21

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun Lesson21Basic() {
    var biogasOutput by remember { mutableFloatStateOf(45f) }
    val animatedProgress by animateFloatAsState(
        targetValue = biogasOutput / 100f,
        animationSpec = tween(durationMillis = 600, easing = LinearEasing),
        label = "biogasProgress"
    )

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Базовий рівень: вихід біогазу з реактора", style = MaterialTheme.typography.titleMedium)
        Text("Поточне значення: %.0f м³/год".format(biogasOutput))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
        )
        Slider(
            value = biogasOutput,
            onValueChange = { biogasOutput = it },
            valueRange = 0f..100f
        )
    }
}

private data class ReactorSnapshot(
    val output: Float,
    val temperature: Float,
    val efficiency: Float
)

@Composable
fun Lesson21Medium() {
    var snapshot by remember {
        mutableStateOf(ReactorSnapshot(output = 52f, temperature = 38f, efficiency = 71f))
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Середній рівень: динамічний монітор установки", style = MaterialTheme.typography.titleMedium)
        BarRow {
            AnimatedBar(
                label = "Вихід газу",
                value = snapshot.output,
                maxValue = 100f,
                unit = "м³/год"
            )
            AnimatedBar(
                label = "Температура",
                value = snapshot.temperature,
                maxValue = 60f,
                unit = "°C",
                warning = snapshot.temperature > 45f
            )
            AnimatedBar(
                label = "Ефективність",
                value = snapshot.efficiency,
                maxValue = 100f,
                unit = "%"
            )
        }
        Button(
            onClick = {
                snapshot = ReactorSnapshot(
                    output = Random.nextInt(20, 95).toFloat(),
                    temperature = Random.nextInt(25, 55).toFloat(),
                    efficiency = Random.nextInt(40, 98).toFloat()
                )
            },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        ) {
            Text("Оновити дані")
        }
    }
}

@Composable
fun Lesson21Advanced() {
    var output by remember { mutableFloatStateOf(50f) }
    var temperature by remember { mutableFloatStateOf(37f) }
    var efficiency by remember { mutableFloatStateOf(75f) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Просунутий рівень: інтерактивний дашборд", style = MaterialTheme.typography.titleMedium)
        Text(
            "Торкніться стовпчика «Вихід газу», щоб імітувати додавання сировини — " +
                "це підвищить вихід і температуру, але тимчасово знизить ефективність.",
            style = MaterialTheme.typography.bodySmall
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InteractiveBar(
                label = "Вихід газу",
                value = output,
                maxValue = 100f,
                unit = "м³/год",
                onTap = {
                    output = (output + Random.nextInt(5, 15)).coerceAtMost(100f)
                    temperature = (temperature + Random.nextInt(1, 4)).coerceAtMost(60f)
                    efficiency = (efficiency - Random.nextInt(2, 8)).coerceAtLeast(10f)
                }
            )
            InteractiveBar(
                label = "Температура",
                value = temperature,
                maxValue = 60f,
                unit = "°C",
                onTap = {
                    temperature = (temperature - Random.nextInt(2, 6)).coerceAtLeast(20f)
                    efficiency = (efficiency + Random.nextInt(1, 5)).coerceAtMost(100f)
                }
            )
            InteractiveBar(
                label = "Ефективність",
                value = efficiency,
                maxValue = 100f,
                unit = "%",
                onTap = {
                    efficiency = (efficiency + Random.nextInt(3, 10)).coerceAtMost(100f)
                    output = (output - Random.nextInt(1, 5)).coerceAtLeast(0f)
                }
            )
        }
        Button(
            onClick = {
                output = 50f; temperature = 37f; efficiency = 75f
            },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        ) {
            Text("Скинути до норми")
        }
    }
}
