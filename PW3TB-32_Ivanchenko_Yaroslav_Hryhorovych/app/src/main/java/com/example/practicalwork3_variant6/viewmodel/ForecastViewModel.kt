package com.example.practicalwork3_variant6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicalwork3_variant6.data.remote.NetworkModule
import com.example.practicalwork3_variant6.data.remote.NetworkResult
import com.example.practicalwork3_variant6.data.remote.UnstableForecastResponse
import com.example.practicalwork3_variant6.data.remote.safeApiCall
import com.example.practicalwork3_variant6.data.remote.safeApiCallWithRetry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel для Заняття №12 — звертається до навмисно "нестабільного" ендпоінту
 * мокованого сервера (/unstable/forecast), який випадково повертає 500/503,
 * "зависає" на 12с (щоб спрацював таймаут) або відповідає успішно.
 */
class ForecastViewModel(application: Application) : AndroidViewModel(application) {

    private val api = NetworkModule.provideApiService(application)
    private val logFile = File(application.filesDir, "network_errors.log")

    private val _isConnected = MutableStateFlow(checkConnectivity(application))
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _result = MutableStateFlow<NetworkResult<UnstableForecastResponse>?>(null)
    val result: StateFlow<NetworkResult<UnstableForecastResponse>?> = _result

    private val _attemptsLog = MutableStateFlow<List<String>>(emptyList())
    val attemptsLog: StateFlow<List<String>> = _attemptsLog

    /** Базовий рівень — перевірка з'єднання перед запитом, без централізованого обробника. */
    fun fetchBasic() {
        val context = getApplication<Application>()
        if (!checkConnectivity(context)) {
            _result.value = NetworkResult.Error(null, "Відсутнє з'єднання з інтернетом")
            return
        }
        viewModelScope.launch {
            _result.value = NetworkResult.Loading
            _result.value = safeApiCall { api.getUnstableForecast() }
        }
    }

    /** Середній рівень — централізований обробник помилок + логування у Logcat. */
    fun fetchMedium() {
        viewModelScope.launch {
            _result.value = NetworkResult.Loading
            val response = safeApiCall { api.getUnstableForecast() }
            if (response is NetworkResult.Error) {
                android.util.Log.e("ForecastViewModel", "Помилка запиту: ${response.message}")
            }
            _result.value = response
        }
    }

    /** Просунутий рівень — retry-логіка з повторними спробами та реєстрацією у файл-журнал. */
    fun fetchAdvanced() {
        viewModelScope.launch {
            _result.value = NetworkResult.Loading
            _attemptsLog.value = emptyList()
            val response = safeApiCallWithRetry(maxAttempts = 3, delayMillis = 600) {
                logAttempt("Спроба запиту /unstable/forecast")
                api.getUnstableForecast()
            }
            if (response is NetworkResult.Error) {
                logToFile("ПОМИЛКА: код=${response.code}, повідомлення=${response.message}")
            }
            _result.value = response
        }
    }

    private fun logAttempt(message: String) {
        val stamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        _attemptsLog.value = _attemptsLog.value + "[$stamp] $message"
    }

    private fun logToFile(message: String) {
        try {
            val stamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            logFile.appendText("[$stamp] $message\n")
        } catch (_: Exception) {
            // Запис у журнал не повинен ламати основний потік роботи застосунку.
        }
    }

    private fun checkConnectivity(context: Application): Boolean {
        val manager = context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE)
                as android.net.ConnectivityManager
        val network = manager.activeNetwork ?: return false
        val capabilities = manager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun refreshConnectivity() {
        _isConnected.value = checkConnectivity(getApplication())
    }
}
