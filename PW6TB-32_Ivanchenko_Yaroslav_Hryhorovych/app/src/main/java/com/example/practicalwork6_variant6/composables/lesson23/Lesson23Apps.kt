package com.example.practicalwork6_variant6.composables.lesson23

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun Lesson23Basic() {
    val context = LocalContext.current
    val storage = remember { MediaStorage(context) }
    var items by remember { mutableStateOf(storage.listAll().filter { it.type == MediaType.IMAGE }) }
    var message by remember { mutableStateOf("") }

    val pickPhoto = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            storage.importFromUri(uri)
                .onSuccess { items = storage.listAll().filter { it.type == MediaType.IMAGE }; message = "Фото додано" }
                .onFailure { message = "Помилка: ${it.message}" }
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Базовий рівень: завантаження та перегляд фотографій", style = MaterialTheme.typography.titleMedium)
        Button(onClick = { pickPhoto.launch("image/*") }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Додати фото")
        }
        Text(message, style = MaterialTheme.typography.labelSmall)
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            items(items) { item ->
                ThumbnailCell(item)
            }
        }
    }
}

@Composable
private fun ThumbnailCell(item: MediaItem) {
    var bitmap by remember(item.file.path) { mutableStateOf<Bitmap?>(null) }
    androidx.compose.runtime.LaunchedEffect(item.file.path) {
        bitmap = loadThumbnail(item.file)
    }
    Column(modifier = Modifier.padding(4.dp)) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = item.name,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f)
            )
        } else {
            Text("…", modifier = Modifier.fillMaxWidth().aspectRatio(1f))
        }
        Text(item.name, style = MaterialTheme.typography.labelSmall, maxLines = 1)
    }
}

private enum class MediaFilter { ALL, PHOTOS, DOCUMENTS }

@Composable
fun Lesson23Medium() {
    val context = LocalContext.current
    val storage = remember { MediaStorage(context) }
    var filter by remember { mutableStateOf(MediaFilter.ALL) }
    var sortByDateDesc by remember { mutableStateOf(true) }
    var all by remember { mutableStateOf(storage.listAll()) }
    var message by remember { mutableStateOf("") }

    val pickAny = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            storage.importFromUri(uri)
                .onSuccess { all = storage.listAll(); message = "Файл додано" }
                .onFailure { message = "Помилка: ${it.message}" }
        }
    }

    val displayed = all
        .filter {
            when (filter) {
                MediaFilter.ALL -> true
                MediaFilter.PHOTOS -> it.type == MediaType.IMAGE
                MediaFilter.DOCUMENTS -> it.type == MediaType.DOCUMENT
            }
        }
        .let { list -> if (sortByDateDesc) list.sortedByDescending { it.addedAt } else list.sortedBy { it.addedAt } }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Середній рівень: фото + документи", style = MaterialTheme.typography.titleMedium)
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Button(onClick = { pickAny.launch("image/*") }) { Text("Додати фото") }
            OutlinedButton(onClick = { pickAny.launch("application/pdf") }, modifier = Modifier.padding(start = 8.dp)) {
                Text("Додати PDF")
            }
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            OutlinedButton(onClick = {
                filter = when (filter) {
                    MediaFilter.ALL -> MediaFilter.PHOTOS
                    MediaFilter.PHOTOS -> MediaFilter.DOCUMENTS
                    MediaFilter.DOCUMENTS -> MediaFilter.ALL
                }
            }) { Text("Фільтр: ${filter.name}") }
            OutlinedButton(onClick = { sortByDateDesc = !sortByDateDesc }, modifier = Modifier.padding(start = 8.dp)) {
                Text(if (sortByDateDesc) "Сортування: нові спершу" else "Сортування: старі спершу")
            }
        }
        Text(message, style = MaterialTheme.typography.labelSmall)
        LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
            items(displayed) { item ->
                val dateStr = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(item.addedAt))
                Text("${item.type}: ${item.name}  ($dateStr)")
            }
        }
    }
}

@Composable
fun Lesson23Advanced() {
    val context = LocalContext.current
    val storage = remember { MediaStorage(context) }
    var objectName by remember { mutableStateOf("Реактор-1") }
    var items by remember { mutableStateOf(storage.listAll()) }
    var message by remember { mutableStateOf("") }
    var renamingItem by remember { mutableStateOf<MediaItem?>(null) }
    var newName by remember { mutableStateOf("") }

    val pickAny = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val safeObject = objectName.ifBlank { "object" }.replace(" ", "_")
            storage.importFromUri(uri, suggestedName = "${safeObject}_${System.currentTimeMillis()}")
                .onSuccess { items = storage.listAll(); message = "Файл додано для «$objectName»" }
                .onFailure { message = "Помилка: ${it.message}" }
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Просунутий рівень: об'єкти, перейменування, сортування", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = objectName, onValueChange = { objectName = it },
            label = { Text("Назва енергетичного об'єкта") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Button(onClick = { pickAny.launch("image/*") }) { Text("Фото об'єкта") }
            OutlinedButton(onClick = { pickAny.launch("*/*") }, modifier = Modifier.padding(start = 8.dp)) {
                Text("Документ об'єкта")
            }
        }
        Text(message, style = MaterialTheme.typography.labelSmall)

        if (renamingItem != null) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                OutlinedTextField(
                    value = newName, onValueChange = { newName = it },
                    label = { Text("Нова назва файлу") },
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    storage.rename(renamingItem!!, newName)
                        .onSuccess { items = storage.listAll(); message = "Перейменовано"; renamingItem = null }
                        .onFailure { message = "Помилка перейменування: ${it.message}" }
                }, modifier = Modifier.padding(start = 8.dp)) { Text("OK") }
            }
        }

        LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
            items(items.sortedByDescending { it.addedAt }) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${item.type}: ${item.name}", modifier = Modifier.weight(1f))
                    IconButton(onClick = { renamingItem = item; newName = item.name }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Перейменувати")
                    }
                    IconButton(onClick = {
                        if (storage.delete(item)) { items = storage.listAll(); message = "Видалено ${item.name}" }
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Видалити")
                    }
                }
            }
        }
    }
}
