package com.example.practicalwork3_variant6.data.remote

/**
 * Результат мережевого запиту — централізована модель обробки помилок
 * (Заняття №12), яка відокремлює UI від деталей винятків Retrofit/OkHttp.
 *
 * Замість того, щоб composable-функції ловили IOException чи HttpException
 * напряму, ViewModel/Repository перетворюють будь-який результат запиту
 * на один із трьох варіантів нижче, а UI лише реагує на готовий стан.
 */
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val code: Int?, val message: String) : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()
}

/**
 * Типи мережевих помилок, визначені за причиною виникнення — дозволяє
 * показувати користувачу зрозуміле повідомлення замість технічного тексту
 * винятку (Заняття №12, "Короткі теоретичні відомості").
 */
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
