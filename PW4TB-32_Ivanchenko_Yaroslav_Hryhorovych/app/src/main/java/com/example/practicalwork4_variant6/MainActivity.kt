package com.example.practicalwork4_variant6

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
import com.example.practicalwork4_variant6.composables.common.DisplayFor
import com.example.practicalwork4_variant6.composables.common.DisplayModeSelector
import com.example.practicalwork4_variant6.composables.common.Lesson
import com.example.practicalwork4_variant6.composables.common.LessonSelector
import com.example.practicalwork4_variant6.composables.lesson13.Lesson13Advanced
import com.example.practicalwork4_variant6.composables.lesson13.Lesson13Basic
import com.example.practicalwork4_variant6.composables.lesson13.Lesson13Medium
import com.example.practicalwork4_variant6.composables.lesson14.Lesson14Advanced
import com.example.practicalwork4_variant6.composables.lesson14.Lesson14Basic
import com.example.practicalwork4_variant6.composables.lesson14.Lesson14Medium
import com.example.practicalwork4_variant6.composables.lesson15.Lesson15Advanced
import com.example.practicalwork4_variant6.composables.lesson15.Lesson15Basic
import com.example.practicalwork4_variant6.composables.lesson15.Lesson15Medium
import com.example.practicalwork4_variant6.composables.lesson16.Lesson16Advanced
import com.example.practicalwork4_variant6.composables.lesson16.Lesson16Basic
import com.example.practicalwork4_variant6.composables.lesson16.Lesson16Medium
import com.example.practicalwork4_variant6.ui.theme.PracticalWork4Variant6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticalWork4Variant6Theme {
                var lesson by remember { mutableStateOf(Lesson.LESSON_13) }
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
                            Lesson.LESSON_13 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson13Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson13Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson13Advanced()
                            }
                            Lesson.LESSON_14 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson14Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson14Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson14Advanced()
                            }
                            Lesson.LESSON_15 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson15Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson15Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson15Advanced()
                            }
                            Lesson.LESSON_16 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson16Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson16Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson16Advanced()
                            }
                        }
                    }
                }
            }
        }
    }
}
