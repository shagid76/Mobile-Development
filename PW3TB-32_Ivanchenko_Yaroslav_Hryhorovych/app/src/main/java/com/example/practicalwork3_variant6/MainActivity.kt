package com.example.practicalwork3_variant6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practicalwork3_variant6.composables.common.DisplayFor
import com.example.practicalwork3_variant6.composables.common.DisplayModeSelector
import com.example.practicalwork3_variant6.composables.common.Lesson
import com.example.practicalwork3_variant6.composables.common.LessonSelector
import com.example.practicalwork3_variant6.composables.lesson10.Lesson10Advanced
import com.example.practicalwork3_variant6.composables.lesson10.Lesson10Basic
import com.example.practicalwork3_variant6.composables.lesson10.Lesson10Medium
import com.example.practicalwork3_variant6.composables.lesson11.Lesson11Advanced
import com.example.practicalwork3_variant6.composables.lesson11.Lesson11Basic
import com.example.practicalwork3_variant6.composables.lesson11.Lesson11Medium
import com.example.practicalwork3_variant6.composables.lesson12.Lesson12Advanced
import com.example.practicalwork3_variant6.composables.lesson12.Lesson12Basic
import com.example.practicalwork3_variant6.composables.lesson12.Lesson12Medium
import com.example.practicalwork3_variant6.composables.lesson9.Lesson9Advanced
import com.example.practicalwork3_variant6.composables.lesson9.Lesson9Basic
import com.example.practicalwork3_variant6.composables.lesson9.Lesson9Medium
import com.example.practicalwork3_variant6.ui.theme.PracticalWork3Variant6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticalWork3Variant6Theme {
                var lesson by remember { mutableStateOf(Lesson.LESSON_9) }
                var displayFor by remember { mutableStateOf(DisplayFor.BASIC_LEVEL) }

                Surface(modifier = Modifier.fillMaxSize()) {
                    Column {
                        LessonSelector(
                            selected = lesson,
                            onSelectedChange = {
                                lesson = it
                                displayFor = DisplayFor.BASIC_LEVEL
                            }
                        )
                        DisplayModeSelector(
                            selected = displayFor,
                            onSelectedChange = { displayFor = it }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        when (lesson) {
                            Lesson.LESSON_9 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson9Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson9Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson9Advanced()
                            }
                            Lesson.LESSON_10 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson10Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson10Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson10Advanced()
                            }
                            Lesson.LESSON_11 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson11Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson11Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson11Advanced()
                            }
                            Lesson.LESSON_12 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson12Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson12Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson12Advanced()
                            }
                        }
                    }
                }
            }
        }
    }
}
