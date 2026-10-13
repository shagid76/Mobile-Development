package com.example.practicalwork5_variant6

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.practicalwork5_variant6.composables.common.DisplayFor
import com.example.practicalwork5_variant6.composables.common.DisplayModeSelector
import com.example.practicalwork5_variant6.composables.common.LessonNumber
import com.example.practicalwork5_variant6.composables.common.LessonSelector
import com.example.practicalwork5_variant6.composables.lesson17.Lesson17Advanced
import com.example.practicalwork5_variant6.composables.lesson17.Lesson17Basic
import com.example.practicalwork5_variant6.composables.lesson17.Lesson17Medium
import com.example.practicalwork5_variant6.composables.lesson18.Lesson18Advanced
import com.example.practicalwork5_variant6.composables.lesson18.Lesson18Basic
import com.example.practicalwork5_variant6.composables.lesson18.Lesson18Medium
import com.example.practicalwork5_variant6.composables.lesson18.createNotificationChannels
import com.example.practicalwork5_variant6.composables.lesson19.Lesson19Advanced
import com.example.practicalwork5_variant6.composables.lesson19.Lesson19Basic
import com.example.practicalwork5_variant6.composables.lesson19.Lesson19Medium
import com.example.practicalwork5_variant6.composables.lesson20.Lesson20Advanced
import com.example.practicalwork5_variant6.composables.lesson20.Lesson20Basic
import com.example.practicalwork5_variant6.composables.lesson20.Lesson20Medium
import com.example.practicalwork5_variant6.ui.theme.Practical_Work5_Variant6Theme
import com.example.practicalwork5_variant6.viewmodel.SensorViewModel

class MainActivity : ComponentActivity() {

    private val sensorViewModel: SensorViewModel by viewModels()
    private val permissionRequestCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ensureNotificationPermission()
        createNotificationChannels(this)

        setContent {
            Practical_Work5_Variant6Theme {
                var lesson by remember { mutableStateOf(LessonNumber.LESSON_17) }
                var displayFor by remember { mutableStateOf(DisplayFor.BASIC_LEVEL) }

                Surface(modifier = Modifier.fillMaxSize()) {
                    Column {
                        LessonSelector(selected = lesson, onSelectedChange = {
                            lesson = it
                            displayFor = DisplayFor.BASIC_LEVEL
                        })
                        DisplayModeSelector(selected = displayFor, onSelectedChange = { displayFor = it })
                        Spacer(modifier = Modifier.height(8.dp))

                        when (lesson) {
                            LessonNumber.LESSON_17 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson17Basic(sensorViewModel)
                                DisplayFor.MIDDLE_LEVEL -> Lesson17Medium(sensorViewModel)
                                DisplayFor.ADVANCED_LEVEL -> Lesson17Advanced(sensorViewModel)
                            }
                            LessonNumber.LESSON_18 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson18Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson18Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson18Advanced()
                            }
                            LessonNumber.LESSON_19 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson19Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson19Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson19Advanced()
                            }
                            LessonNumber.LESSON_20 -> when (displayFor) {
                                DisplayFor.BASIC_LEVEL -> Lesson20Basic()
                                DisplayFor.MIDDLE_LEVEL -> Lesson20Medium()
                                DisplayFor.ADVANCED_LEVEL -> Lesson20Advanced()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun ensureNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), permissionRequestCode
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode) {
            val granted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            Toast.makeText(
                this,
                if (granted) "Дозвіл на сповіщення надано" else "Дозвіл на сповіщення відхилено",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
