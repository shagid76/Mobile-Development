package com.example.practicalwork6_variant6.composables.lesson23

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

/**
 * Базова оптимізація розмірів зображень (Заняття 23): замість завантаження
 * повнорозмірного фото в пам'ять, обчислюється коефіцієнт зменшення
 * ([inSampleSize]) так, щоб мініатюра не перевищувала [reqSize] пікселів —
 * це економить пам'ять пристрою при відображенні галереї.
 */
fun loadThumbnail(file: File, reqSize: Int = 200): Bitmap? {
    return try {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(file.absolutePath, bounds)

        var sample = 1
        while (bounds.outWidth / sample > reqSize * 2 || bounds.outHeight / sample > reqSize * 2) {
            sample *= 2
        }
        val options = BitmapFactory.Options().apply { inSampleSize = sample }
        BitmapFactory.decodeFile(file.absolutePath, options)
    } catch (e: Exception) {
        null
    }
}
