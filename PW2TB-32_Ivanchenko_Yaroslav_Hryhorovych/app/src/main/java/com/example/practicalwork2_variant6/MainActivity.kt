package com.example.practicalwork2_variant6

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
import com.example.practicalwork2_variant6.composables.common.DisplayFor
import com.example.practicalwork2_variant6.composables.common.DisplayModeSelector
import com.example.practicalwork2_variant6.composables.common.Lesson
import com.example.practicalwork2_variant6.composables.common.LessonSelector
import com.example.practicalwork2_variant6.composables.lesson5.AdvancedNavApp
import com.example.practicalwork2_variant6.composables.lesson5.BasicNavApp
import com.example.practicalwork2_variant6.composables.lesson5.MediumNavApp
import com.example.practicalwork2_variant6.composables.lesson6.AdvancedFormApp
import com.example.practicalwork2_variant6.composables.lesson6.BasicFormApp
import com.example.practicalwork2_variant6.composables.lesson6.MediumFormApp
import com.example.practicalwork2_variant6.composables.lesson7.AdvancedViewModelApp
import com.example.practicalwork2_variant6.composables.lesson7.BasicViewModelApp
import com.example.practicalwork2_variant6.composables.lesson7.MediumViewModelApp
import com.example.practicalwork2_variant6.composables.lesson8.AdvancedRoomApp
import com.example.practicalwork2_variant6.composables.lesson8.BasicRoomApp
import com.example.practicalwork2_variant6.composables.lesson8.MediumRoomApp
import com.example.practicalwork2_variant6.ui.theme.PracticalWork2Variant6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticalWork2Variant6Theme {
                var lesson by remember { mutableStateOf(Lesson.LESSON_5) }
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
                            Lesson.LESSON_5 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> BasicNavApp()
                                DisplayFor.MIDDLE_LEVEL -> MediumNavApp()
                                DisplayFor.ADVANCED_LEVEL -> AdvancedNavApp()
                            }
                            Lesson.LESSON_6 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> BasicFormApp()
                                DisplayFor.MIDDLE_LEVEL -> MediumFormApp()
                                DisplayFor.ADVANCED_LEVEL -> AdvancedFormApp()
                            }
                            Lesson.LESSON_7 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> BasicViewModelApp()
                                DisplayFor.MIDDLE_LEVEL -> MediumViewModelApp()
                                DisplayFor.ADVANCED_LEVEL -> AdvancedViewModelApp()
                            }
                            Lesson.LESSON_8 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> BasicRoomApp()
                                DisplayFor.MIDDLE_LEVEL -> MediumRoomApp()
                                DisplayFor.ADVANCED_LEVEL -> AdvancedRoomApp()
                            }
                        }
                    }
                }
            }
        }
    }
}
