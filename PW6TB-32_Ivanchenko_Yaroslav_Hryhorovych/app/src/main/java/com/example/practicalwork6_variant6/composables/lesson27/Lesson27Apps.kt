package com.example.practicalwork6_variant6.composables.lesson27

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val APK_BUNDLE_CHECKLIST = listOf(
    "Підвищити versionCode і versionName у build.gradle.kts",
    "Увімкнути isMinifyEnabled та підключити ProGuard/R8-правила",
    "Налаштувати signingConfig для release-збірки (keystore поза репозиторієм)",
    "Зібрати Android App Bundle: ./gradlew bundleRelease",
    "Перевірити APK з bundletool перед публікацією"
)

@Composable
fun Lesson27Basic() {
    val context = LocalContext.current
    val packageInfo = remember {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0)
        }.getOrNull()
    }
    @Suppress("DEPRECATION")
    val versionCodeText = packageInfo?.versionCode?.toString() ?: "н/д"
    val checked = remember { mutableStateMapOf<String, Boolean>() }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Базовий рівень: збірка APK/Bundle", style = MaterialTheme.typography.titleMedium)
        Text("Пакет: ${context.packageName}")
        Text("Версія: ${packageInfo?.versionName ?: "н/д"} (код $versionCodeText)")
        Text(
            "APK — одноразовий встановлювальний пакет; Android App Bundle (AAB) — " +
                "формат для Play Market, з якого Google Play сам генерує оптимізовані APK " +
                "під конкретний пристрій.",
            modifier = Modifier.padding(top = 8.dp)
        )
        ChecklistSection("Чекліст збірки", APK_BUNDLE_CHECKLIST, checked)
    }
}

private val SIGNING_CHECKLIST = listOf(
    "Створити keystore (keytool -genkeypair ...)",
    "Зберігати пароль keystore поза git (local.properties / змінні середовища)",
    "Додати signingConfigs { release { ... } } у app/build.gradle.kts",
    "Прив'язати buildTypes.release.signingConfig до release",
    "Перевірити підпис зібраного APK: apksigner verify"
)

@Composable
fun Lesson27Medium() {
    val checked = remember { mutableStateMapOf<String, Boolean>() }
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Середній рівень: підписання застосунку", style = MaterialTheme.typography.titleMedium)
        Text(
            "Приклад структури у build.gradle.kts (значення — через local.properties, " +
                "ніколи не в репозиторії):",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            "signingConfigs { create(\"release\") { storeFile = file(keystoreProps[\"storeFile\"]) ... } }",
            style = MaterialTheme.typography.labelSmall
        )
        ChecklistSection("Чекліст підписання", SIGNING_CHECKLIST, checked)
    }
}

private val PUBLISH_CHECKLIST = listOf(
    "Зареєструвати застосунок у Google Play Console",
    "Заповнити картку застосунку (назва, опис, скриншоти, іконка)",
    "Пройти Data Safety / Privacy Policy форму",
    "Завантажити підписаний AAB у внутрішній тест-трек",
    "Пройти перевірку Google Play та опублікувати у production"
)

private val PRESENTATION_SECTIONS = listOf(
    "Тема й актуальність: моніторинг біогазової установки",
    "Архітектура застосунку (MVVM, Jetpack Compose)",
    "Демонстрація ключових занять 21-27",
    "Складнощі та їх вирішення",
    "Висновки та подальший розвиток проєкту"
)

@Composable
fun Lesson27Advanced() {
    val publishChecked = remember { mutableStateMapOf<String, Boolean>() }
    var presentationNotes by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Просунутий рівень: публікація та презентація", style = MaterialTheme.typography.titleMedium)
        ChecklistSection("Чекліст публікації у Play Market", PUBLISH_CHECKLIST, publishChecked)

        Text("План презентації проєкту:", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 12.dp))
        LazyColumn {
            items(PRESENTATION_SECTIONS) { section ->
                Text("• $section", style = MaterialTheme.typography.bodySmall)
            }
        }
        val donePublish = publishChecked.values.count { it }
        Text(
            "Готовність до публікації: $donePublish з ${PUBLISH_CHECKLIST.size} пунктів",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun ChecklistSection(
    title: String,
    items: List<String>,
    checked: androidx.compose.runtime.snapshots.SnapshotStateMap<String, Boolean>
) {
    Text(title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 12.dp))
    Column {
        items.forEach { item ->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                Checkbox(
                    checked = checked[item] == true,
                    onCheckedChange = { checked[item] = it }
                )
                Text(item, modifier = Modifier.padding(top = 12.dp))
            }
        }
    }
}
