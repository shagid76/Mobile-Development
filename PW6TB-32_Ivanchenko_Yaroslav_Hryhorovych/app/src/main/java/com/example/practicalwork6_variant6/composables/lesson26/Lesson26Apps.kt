package com.example.practicalwork6_variant6.composables.lesson26

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.practicalwork6_variant6.composables.common.SimpleLineChart
import com.example.practicalwork6_variant6.domain.ResourceEvaluator
import com.example.practicalwork6_variant6.domain.ResourceStatus
import kotlinx.coroutines.delay

private fun statusColor(status: ResourceStatus): Color = when (status) {
    ResourceStatus.NORMAL -> Color(0xFF2E7D32)
    ResourceStatus.WARNING -> Color(0xFFF9A825)
    ResourceStatus.CRITICAL -> Color(0xFFC62828)
}

@Composable
fun Lesson26Basic() {
    val context = LocalContext.current
    val monitor = remember { ResourceMonitor(context) }
    var snapshot by remember { mutableStateOf(monitor.snapshot()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1500)
            snapshot = monitor.snapshot()
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Базовий рівень: моніторинг ресурсів", style = MaterialTheme.typography.titleMedium)
        LabeledProgress("CPU (застосунок)", snapshot.cpuPercent)
        LabeledProgress("Пам'ять пристрою", snapshot.memoryPercent)
        Text("Батарея: ${if (snapshot.batteryPercent >= 0) "${snapshot.batteryPercent}%" else "н/д"}")
    }
}

@Composable
private fun LabeledProgress(label: String, percent: Float) {
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text("$label: %.1f%%".format(percent), style = MaterialTheme.typography.bodySmall)
        LinearProgressIndicator(progress = { percent / 100f }, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun Lesson26Medium() {
    val context = LocalContext.current
    val monitor = remember { ResourceMonitor(context) }
    var snapshot by remember { mutableStateOf(monitor.snapshot()) }
    var darkTheme by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1500)
            snapshot = monitor.snapshot()
        }
    }

    val localColorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = localColorScheme) {
        Surface {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("Середній рівень: кольорові індикатори", style = MaterialTheme.typography.titleMedium)
                }
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text("Темна тема")
                    Switch(checked = darkTheme, onCheckedChange = { darkTheme = it })
                }
                ColorIndicator("CPU (застосунок)", snapshot.cpuPercent)
                ColorIndicator("Пам'ять пристрою", snapshot.memoryPercent)
                Text("Мережа: ${snapshot.networkStatus}", modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
private fun ColorIndicator(label: String, percent: Float) {
    val status = ResourceEvaluator.classify(percent)
    val color by animateColorAsState(targetValue = statusColor(status), animationSpec = tween(400), label = "indicatorColor")
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text("$label: %.1f%% (${status.name})".format(percent), style = MaterialTheme.typography.bodySmall)
        LinearProgressIndicator(
            progress = { percent / 100f },
            color = color,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Lesson26Advanced() {
    val context = LocalContext.current
    val monitor = remember { ResourceMonitor(context) }
    var snapshot by remember { mutableStateOf(monitor.snapshot()) }
    var darkTheme by remember { mutableStateOf(false) }
    val cpuHistory = remember { mutableStateListOf<Float>() }
    val memHistory = remember { mutableStateListOf<Float>() }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1200)
            snapshot = monitor.snapshot()
            cpuHistory.add(snapshot.cpuPercent)
            memHistory.add(snapshot.memoryPercent)
            if (cpuHistory.size > 20) cpuHistory.removeAt(0)
            if (memHistory.size > 20) memHistory.removeAt(0)
        }
    }

    val localColorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = localColorScheme) {
        Surface {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Просунутий рівень: дашборд ресурсів", style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text("Темна тема")
                    Switch(checked = darkTheme, onCheckedChange = { darkTheme = it })
                }
                ColorIndicator("CPU (застосунок)", snapshot.cpuPercent)
                Text("Історія CPU, %", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
                SimpleLineChart(values = cpuHistory.toList())

                ColorIndicator("Пам'ять пристрою", snapshot.memoryPercent)
                Text("Історія пам'яті, %", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
                SimpleLineChart(values = memHistory.toList())

                Text(
                    "Батарея: ${if (snapshot.batteryPercent >= 0) "${snapshot.batteryPercent}%" else "н/д"}  •  " +
                        "Мережа: ${snapshot.networkStatus}",
                    modifier = Modifier.padding(top = 8.dp)
                )
                Button(onClick = { cpuHistory.clear(); memHistory.clear() }, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Очистити історію")
                }
            }
        }
    }
}
