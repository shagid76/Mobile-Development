package com.example.practicalwork5_variant6.composables.lesson18

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object NotificationActionState {
    private val _lastAction = MutableStateFlow<String?>(null)
    val lastAction: StateFlow<String?> = _lastAction.asStateFlow()

    fun update(action: String) {
        _lastAction.value = action
    }
}

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            NotificationIds.ACTION_CONFIRM -> NotificationActionState.update("Подія підтверджена")
            NotificationIds.ACTION_POSTPONE -> NotificationActionState.update("Подію відкладено")
        }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.cancel(NotificationIds.ACTION_NOTIFICATION_ID)
    }
}
