package com.example.practicalwork5_variant6.composables.lesson18

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationIds {
    const val BASIC_CHANNEL = "basic_channel"
    const val TEMPERATURE_CHANNEL = "temperature_channel"
    const val ACTION_CHANNEL = "action_channel"

    const val BASIC_NOTIFICATION_ID = 201
    const val TEMPERATURE_NOTIFICATION_ID = 202
    const val PRESSURE_NOTIFICATION_ID = 203
    const val ACTION_NOTIFICATION_ID = 204

    const val ACTION_CONFIRM = "com.example.practicalwork5_variant6.ACTION_CONFIRM"
    const val ACTION_POSTPONE = "com.example.practicalwork5_variant6.ACTION_POSTPONE"
    const val EXTRA_SOURCE = "extra_source"
}

fun createNotificationChannels(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val manager = context.getSystemService(NotificationManager::class.java)

    val basic = NotificationChannel(
        NotificationIds.BASIC_CHANNEL, "Базові сповіщення", NotificationManager.IMPORTANCE_DEFAULT
    ).apply { description = "Прості сповіщення про події застосунку" }

    val temperature = NotificationChannel(
        NotificationIds.TEMPERATURE_CHANNEL, "Сповіщення про процес", NotificationManager.IMPORTANCE_HIGH
    ).apply { description = "Перевищення порогів температури/тиску процесу" }

    val action = NotificationChannel(
        NotificationIds.ACTION_CHANNEL, "Інтерактивні сповіщення", NotificationManager.IMPORTANCE_HIGH
    ).apply { description = "Сповіщення з діями (підтвердити/відкласти)" }

    manager.createNotificationChannel(basic)
    manager.createNotificationChannel(temperature)
    manager.createNotificationChannel(action)
}

fun sendBasicNotification(context: Context) {
    val notification = NotificationCompat.Builder(context, NotificationIds.BASIC_CHANNEL)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Біогазова установка")
        .setContentText("Застосунок запущено, моніторинг активний")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()
    NotificationManagerCompat.from(context).notify(NotificationIds.BASIC_NOTIFICATION_ID, notification)
}

fun sendCategorizedNotification(context: Context, title: String, text: String, notificationId: Int) {
    val notification = NotificationCompat.Builder(context, NotificationIds.TEMPERATURE_CHANNEL)
        .setSmallIcon(android.R.drawable.ic_dialog_alert)
        .setContentTitle(title)
        .setContentText(text)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()
    NotificationManagerCompat.from(context).notify(notificationId, notification)
}

fun sendInteractiveNotification(context: Context) {
    fun actionIntent(action: String): PendingIntent {
        val intent = Intent(context, NotificationActionReceiver::class.java).apply {
            this.action = action
            putExtra(NotificationIds.EXTRA_SOURCE, "reactor_alert")
        }
        return PendingIntent.getBroadcast(
            context,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    val notification = NotificationCompat.Builder(context, NotificationIds.ACTION_CHANNEL)
        .setSmallIcon(android.R.drawable.ic_dialog_alert)
        .setContentTitle("Сонячна станція – зараз ★")
        .setContentText("Реактор: перевищено пороговий тиск біогазу")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .addAction(0, "Підтвердити", actionIntent(NotificationIds.ACTION_CONFIRM))
        .addAction(0, "Відкласти", actionIntent(NotificationIds.ACTION_POSTPONE))
        .setAutoCancel(true)
        .build()
    NotificationManagerCompat.from(context).notify(NotificationIds.ACTION_NOTIFICATION_ID, notification)
}
