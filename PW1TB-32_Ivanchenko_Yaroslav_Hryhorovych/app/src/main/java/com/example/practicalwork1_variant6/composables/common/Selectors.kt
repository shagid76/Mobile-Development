package com.example.practicalwork1_variant6.composables.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LessonSelector(
    selected: Lesson,
    onSelectedChange: (Lesson) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Практична робота №1 — Варіант 6: Біогазова установка",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Lesson.values().forEach { lesson ->
            Button(
                onClick = { onSelectedChange(lesson) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                enabled = lesson != selected
            ) {
                Text(
                    text = when (lesson) {
                        Lesson.LESSON_1 -> "Заняття 1: Hello World"
                        Lesson.LESSON_2 -> "Заняття 2: Gradle і структура"
                        Lesson.LESSON_3 -> "Заняття 3: Compose основи"
                        Lesson.LESSON_4 -> "Заняття 4: Авторизація"
                    }
                )
            }
        }
    }
}

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
            text = "Вибір рівня складності",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
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
