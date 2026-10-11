package com.example.practicalwork1_variant6

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
import com.example.practicalwork1_variant6.composables.common.DisplayFor
import com.example.practicalwork1_variant6.composables.common.DisplayModeSelector
import com.example.practicalwork1_variant6.composables.common.Lesson
import com.example.practicalwork1_variant6.composables.common.LessonSelector
import com.example.practicalwork1_variant6.composables.lesson1.BiogasAppLevel1
import com.example.practicalwork1_variant6.composables.lesson1.BiogasAppLevel2
import com.example.practicalwork1_variant6.composables.lesson1.BiogasAppLevel3
import com.example.practicalwork1_variant6.composables.lesson2.AdvancedBiogasApp
import com.example.practicalwork1_variant6.composables.lesson2.BasicBiogasApp
import com.example.practicalwork1_variant6.composables.lesson2.MediumBiogasApp
import com.example.practicalwork1_variant6.composables.lesson3.AdvancedProcessApp
import com.example.practicalwork1_variant6.composables.lesson3.BasicProcessApp
import com.example.practicalwork1_variant6.composables.lesson3.MediumProcessApp
import com.example.practicalwork1_variant6.composables.lesson4.AdvancedAuthApp
import com.example.practicalwork1_variant6.composables.lesson4.BasicAuthApp
import com.example.practicalwork1_variant6.composables.lesson4.MediumAuthApp
import com.example.practicalwork1_variant6.ui.theme.PracticalWork1Variant6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticalWork1Variant6Theme {
                var lesson by remember { mutableStateOf(Lesson.LESSON_1) }
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
                            Lesson.LESSON_1 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> BiogasAppLevel1()
                                DisplayFor.MIDDLE_LEVEL -> BiogasAppLevel2()
                                DisplayFor.ADVANCED_LEVEL -> BiogasAppLevel3()
                            }
                            Lesson.LESSON_2 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> BasicBiogasApp()
                                DisplayFor.MIDDLE_LEVEL -> MediumBiogasApp()
                                DisplayFor.ADVANCED_LEVEL -> AdvancedBiogasApp()
                            }
                            Lesson.LESSON_3 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> BasicProcessApp()
                                DisplayFor.MIDDLE_LEVEL -> MediumProcessApp()
                                DisplayFor.ADVANCED_LEVEL -> AdvancedProcessApp()
                            }
                            Lesson.LESSON_4 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> BasicAuthApp()
                                DisplayFor.MIDDLE_LEVEL -> MediumAuthApp()
                                DisplayFor.ADVANCED_LEVEL -> AdvancedAuthApp()
                            }
                        }
                    }
                }
            }
        }
    }
}
