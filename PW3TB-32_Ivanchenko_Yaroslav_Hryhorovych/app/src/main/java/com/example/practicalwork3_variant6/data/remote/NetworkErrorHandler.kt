package com.example.practicalwork3_variant6.data.remote

import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Централізований обробник мережевих помилок (Заняття №12, середній/просунутий рівні).
 *
 * Будь-який виклик Retrofit в проєкті проходить через [safeApiCall], що перетворює
 * виняток або неуспішну HTTP-відповідь у [NetworkResult.Error] з готовим,
 * зрозумілим користувачу повідомленням — UI-шар більше не має справи з
 * IOException, HttpException чи кодами відповіді напряму.
 */
object NetworkErrorHandler {

    fun classify(throwable: Throwable): NetworkErrorType = when (throwable) {
        is UnknownHostException -> NetworkErrorType.NO_INTERNET
        is SocketTimeoutException -> NetworkErrorType.TIMEOUT
        is IOException -> NetworkErrorType.NO_INTERNET
        else -> NetworkErrorType.UNKNOWN
    }

    fun classifyHttpCode(code: Int): NetworkErrorType = when (code) {
        in 500..599 -> NetworkErrorType.SERVER_ERROR
        in 400..499 -> NetworkErrorType.CLIENT_ERROR
        else -> NetworkErrorType.UNKNOWN
    }
}

/**
 * Безпечно виконує мережевий виклик [call] та повертає [NetworkResult],
 * ніколи не кидаючи виняток далі — весь мережевий шар застосунку
 * повинен проходити через цю функцію (Заняття №12).
 */
suspend fun <T> safeApiCall(call: suspend () -> Response<T>): NetworkResult<T> {
    return try {
        val response = call()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkResult.Success(body)
        } else {
            val errorType = NetworkErrorHandler.classifyHttpCode(response.code())
            NetworkResult.Error(response.code(), errorType.userMessage(response.code()))
        }
    } catch (throwable: Throwable) {
        val errorType = NetworkErrorHandler.classify(throwable)
        NetworkResult.Error(code = null, message = errorType.userMessage())
    }
}

/**
 * Варіант [safeApiCall] з повторними спробами (retry) при тимчасових помилках
 * мережі або сервера — просунутий рівень Заняття №12.
 *
 * @param maxAttempts максимальна кількість спроб (включно з першою).
 * @param delayMillis базова затримка між спробами в мілісекундах.
 */
suspend fun <T> safeApiCallWithRetry(
    maxAttempts: Int = 3,
    delayMillis: Long = 800,
    call: suspend () -> Response<T>
): NetworkResult<T> {
    var lastResult: NetworkResult<T> = NetworkResult.Error(null, "Запит не було виконано")
    repeat(maxAttempts) { attempt ->
        val result = safeApiCall(call)
        if (result is NetworkResult.Success) return result
        lastResult = result
        // Повторюємо лише для тимчасових помилок (5xx, таймаут, відсутність мережі),
        // а не для помилок клієнта (4xx), які повторна спроба не виправить.
        val isRetryable = result is NetworkResult.Error &&
            (result.code == null || result.code in 500..599)
        if (isRetryable && attempt < maxAttempts - 1) {
            kotlinx.coroutines.delay(delayMillis * (attempt + 1)) // проста експоненційна затримка
        } else {
            return lastResult
        }
    }
    return lastResult
}
