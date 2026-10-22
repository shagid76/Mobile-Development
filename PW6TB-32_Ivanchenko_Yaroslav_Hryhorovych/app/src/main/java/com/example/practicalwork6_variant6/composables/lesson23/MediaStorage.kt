package com.example.practicalwork6_variant6.composables.lesson23

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File

enum class MediaType { IMAGE, DOCUMENT, OTHER }

data class MediaItem(
    val file: File,
    val type: MediaType,
    val addedAt: Long
) {
    val name: String get() = file.name
    val uri: Uri get() = Uri.fromFile(file)
}

class MediaStorage(private val context: Context) {

    private val dir = File(context.filesDir, "media").apply { mkdirs() }

    fun importFromUri(sourceUri: Uri, suggestedName: String? = null): Result<MediaItem> = runCatching {
        val resolver = context.contentResolver
        val mime = resolver.getType(sourceUri)
        val extension = mime?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) } ?: "bin"
        val name = suggestedName?.takeIf { it.isNotBlank() }
            ?: "object_${System.currentTimeMillis()}.$extension"
        val target = File(dir, name)
        resolver.openInputStream(sourceUri)?.use { input ->
            target.outputStream().use { output -> input.copyTo(output) }
        } ?: error("Не вдалося відкрити обраний файл")
        MediaItem(target, classify(mime), target.lastModified())
    }

    private fun classify(mime: String?): MediaType = when {
        mime == null -> MediaType.OTHER
        mime.startsWith("image/") -> MediaType.IMAGE
        mime == "application/pdf" ||
            mime == "application/msword" ||
            mime.contains("wordprocessingml") -> MediaType.DOCUMENT
        else -> MediaType.OTHER
    }

    fun listAll(): List<MediaItem> = dir.listFiles()
        ?.filter { it.isFile }
        ?.map { f ->
            val mime = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(f.extension.lowercase())
            MediaItem(f, classify(mime), f.lastModified())
        }
        ?: emptyList()

    fun rename(item: MediaItem, newName: String): Result<MediaItem> = runCatching {
        val safeName = newName.ifBlank { item.name }
        val target = File(dir, safeName)
        if (!item.file.renameTo(target)) error("Перейменування не вдалося")
        item.copy(file = target)
    }

    fun delete(item: MediaItem): Boolean = item.file.exists() && item.file.delete()

    fun shareableUri(item: MediaItem): Uri = FileProvider.getUriForFile(
        context, "${context.packageName}.fileprovider", item.file
    )
}
