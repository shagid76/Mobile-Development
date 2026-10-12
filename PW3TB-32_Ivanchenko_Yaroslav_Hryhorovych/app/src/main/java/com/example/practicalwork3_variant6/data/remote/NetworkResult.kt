package com.example.practicalwork3_variant6.data.remote

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val code: Int?, val message: String) : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()
}

enum class NetworkErrorType {
    NO_INTERNET,
    TIMEOUT,
    SERVER_ERROR,      // 5xx
    CLIENT_ERROR,      // 4xx (401, 403, 404 ...)
    UNKNOWN;

    fun userMessage(httpCode: Int? = null): String = when (this) {
        NO_INTERNET -> "Відсутнє з'єднання з інтернетом. Перевірте мережу і спробуйте ще раз."
        TIMEOUT -> "Сервер не відповідає занадто довго. Спробуйте пізніше."
        SERVER_ERROR -> "Сталася помилка на сервері (код $httpCode). Спробуйте пізніше."
        CLIENT_ERROR -> when (httpCode) {
            401 -> "Потрібна авторизація — увійдіть у систему знову."
            403 -> "Доступ заборонено для вашої ролі."
            404 -> "Дані не знайдено."
            else -> "Некоректний запит (код $httpCode)."
        }
        UNKNOWN -> "Невідома помилка. Спробуйте ще раз."
    }
}
