package com.example.practicalwork3_variant6.data.remote

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp-перехоплювач (Interceptor), що автоматично додає заголовок
 * `Authorization: Bearer <token>` до кожного запиту, якщо токен збережено
 * у [TokenStore] — Заняття №11, середній та просунутий рівні.
 *
 * Завдяки цьому composable-функціям і ViewModel не потрібно вручну передавати
 * токен у кожен виклик Retrofit — авторизація відбувається "прозоро" на рівні мережі.
 */
class AuthInterceptor(private val tokenStore: TokenStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // runBlocking тут прийнятний, бо OkHttp-перехоплювачі самі працюють
        // у фоновому потоці мережевого запиту, а не в UI-потоці.
        val token = runBlocking { tokenStore.currentToken() }

        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}
