package com.example.practicalwork3_variant6.data.remote

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.tokenDataStore by preferencesDataStore(name = "auth_token_store")

/**
 * Локальне сховище токена автентифікації на базі Jetpack DataStore
 * (сучасна заміна SharedPreferences) — Заняття №11.
 *
 * Зберігає мокований токен і роль користувача між запусками застосунку,
 * а [AuthInterceptor] читає його для автоматичної підстановки у заголовки запитів.
 */
class TokenStore(private val context: Context) {

    private val tokenKey = stringPreferencesKey("auth_token")
    private val roleKey = stringPreferencesKey("auth_role")

    val tokenFlow: Flow<String?> = context.tokenDataStore.data.map { it[tokenKey] }
    val roleFlow: Flow<String?> = context.tokenDataStore.data.map { it[roleKey] }

    suspend fun save(token: String, role: String) {
        context.tokenDataStore.edit { prefs ->
            prefs[tokenKey] = token
            prefs[roleKey] = role
        }
    }

    suspend fun clear() {
        context.tokenDataStore.edit { prefs ->
            prefs.remove(tokenKey)
            prefs.remove(roleKey)
        }
    }

    /** Синхронне читання поточного токена — використовується у [AuthInterceptor]. */
    suspend fun currentToken(): String? = tokenFlow.first()
}
