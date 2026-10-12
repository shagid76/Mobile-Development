package com.example.practicalwork3_variant6.data.remote

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Створює та налаштовує Retrofit-клієнт для звернення до мокованого
 * REST API біогазової установки (Заняття №10-12).
 *
 * 10.0.2.2 — спеціальна адреса, якою Android-емулятор звертається до
 * localhost хост-машини, де запущено mock-server/server.js (порт 5055).
 * Для фізичного пристрою в тій самій Wi-Fi мережі замініть на IP-адресу
 * комп'ютера, наприклад "http://192.168.0.105:5055/".
 */
object NetworkModule {

    private const val BASE_URL = "http://10.0.2.2:5055/"

    /** Таймаут з'єднання/читання — навмисно короткий, щоб продемонструвати
     *  обробку таймаутів на нестабільному ендпоінті (Заняття №12). */
    private const val TIMEOUT_SECONDS = 6L

    fun provideApiService(context: Context): BiogasApiService {
        val tokenStore = TokenStore(context.applicationContext)

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenStore))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(BiogasApiService::class.java)
    }

    fun provideTokenStore(context: Context): TokenStore =
        TokenStore(context.applicationContext)
}
