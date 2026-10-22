package com.example.practicalwork6_variant6

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.practicalwork6_variant6.composables.lesson21.Lesson21Medium
import com.example.practicalwork6_variant6.ui.theme.Practical_Work6_Variant6Theme
import org.junit.Rule
import org.junit.Test

clas Lesson21UiTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun updateButton_isDisplayed_andClickable() {
        composeRule.setContent {
            Practical_Work6_Variant6Theme {
                Lesson21Medium()
            }
        }

        composeRule.onNodeWithText("Оновити дані").assertIsDisplayed()
        composeRule.onNodeWithText("Оновити дані").performClick()
    }
}
