package com.example.practicalwork5_variant6.composables.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DisplayModeSelector(
    selected: DisplayFor,
    onSelectedChange: (DisplayFor) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Вибір рівня відображення",
            style = MaterialTheme.typography.bodyLarge
        )
        DisplayFor.values().forEach { mode ->
            Button(
                onClick = { onSelectedChange(mode) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                enabled = mode != selected
            ) {
                Text(
                    text = when (mode) {
                        DisplayFor.BASIC_LEVEL -> "Базовий рівень"
                        DisplayFor.MIDDLE_LEVEL -> "Середній рівень"
                        DisplayFor.ADVANCED_LEVEL -> "Просунутий рівень"
                    }
                )
            }
        }
    }
}

@Composable
fun LessonSelector(
    selected: LessonNumber,
    onSelectedChange: (LessonNumber) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        LessonNumber.values().forEach { lesson ->
            Button(
                onClick = { onSelectedChange(lesson) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                enabled = lesson != selected
            ) {
                Text(text = lesson.title)
            }
        }
    }
}
