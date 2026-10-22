package com.example.practicalwork6_variant6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practicalwork6_variant6.composables.common.DisplayFor
import com.example.practicalwork6_variant6.composables.common.DisplayModeSelector
import com.example.practicalwork6_variant6.composables.common.LessonNumber
import com.example.practicalwork6_variant6.composables.common.LessonSelector
import com.example.practicalwork6_variant6.composables.lesson21.Lesson21Advanced
import com.example.practicalwork6_variant6.composables.lesson21.Lesson21Basic
import com.example.practicalwork6_variant6.composables.lesson21.Lesson21Medium
import com.example.practicalwork6_variant6.composables.lesson22.Lesson22Advanced
import com.example.practicalwork6_variant6.composables.lesson22.Lesson22Basic
import com.example.practicalwork6_variant6.composables.lesson22.Lesson22Medium
import com.example.practicalwork6_variant6.composables.lesson23.Lesson23Advanced
import com.example.practicalwork6_variant6.composables.lesson23.Lesson23Basic
import com.example.practicalwork6_variant6.composables.lesson23.Lesson23Medium
import com.example.practicalwork6_variant6.composables.lesson24.Lesson24Advanced
import com.example.practicalwork6_variant6.composables.lesson24.Lesson24Basic
import com.example.practicalwork6_variant6.composables.lesson24.Lesson24Medium
import com.example.practicalwork6_variant6.composables.lesson25.Lesson25Advanced
import com.example.practicalwork6_variant6.composables.lesson25.Lesson25Basic
import com.example.practicalwork6_variant6.composables.lesson25.Lesson25Medium
import com.example.practicalwork6_variant6.composables.lesson26.Lesson26Advanced
import com.example.practicalwork6_variant6.composables.lesson26.Lesson26Basic
import com.example.practicalwork6_variant6.composables.lesson26.Lesson26Medium
import com.example.practicalwork6_variant6.composables.lesson27.Lesson27Advanced
import com.example.practicalwork6_variant6.composables.lesson27.Lesson27Basic
import com.example.practicalwork6_variant6.composables.lesson27.Lesson27Medium
import com.example.practicalwork6_variant6.ui.theme.Practical_Work6_Variant6Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Practical_Work6_Variant6Theme {
                var lesson by remember { mutableStateOf(LessonNumber.LESSON_21) }
                var displayFor by remember { mutableStateOf(DisplayFor.BASIC_LEVEL) }

                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        LessonSelector(selected = lesson, onSelectedChange = {
                            lesson = it
                            displayFor = DisplayFor.BASIC_LEVEL
                        })
                        DisplayModeSelector(selected = displayFor, onSelectedChange = { displayFor = it })
                        Spacer(modifier = Modifier.height(8.dp))

                        when (lesson) {
                            LessonNumber.LESSON_21 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson21Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson21Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson21Advanced()
                            }
                            LessonNumber.LESSON_22 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson22Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson22Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson22Advanced()
                            }
                            LessonNumber.LESSON_23 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson23Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson23Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson23Advanced()
                            }
                            LessonNumber.LESSON_24 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson24Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson24Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson24Advanced()
                            }
                            LessonNumber.LESSON_25 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson25Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson25Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson25Advanced()
                            }
                            LessonNumber.LESSON_26 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson26Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson26Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson26Advanced()
                            }
                            LessonNumber.LESSON_27 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson27Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson27Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson27Advanced()
                            }
                        }
                    }
                }
            }
        }
    }
}
