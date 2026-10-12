package com.example.practicalwork3_variant6.data.remote

import com.example.practicalwork3_variant6.domain.ReactorState

/**
 * DTO (Data Transfer Object) показника реактора, що відповідає JSON-структурі
 * мокованого REST API (дивись mock-server/server.js, ендпоінт GET /readings).
 * Gson автоматично десеріалізує відповідь сервера в об'єкти цього класу.
 */
data class ReactorReadingDto(
    val id: Int,
    val fillLevel: Int,
    val methaneConcentration: Int,
    val temperature: Int,
    val pressure: Int,
    val timestamp: Long
)

/** Перетворення DTO (мережева модель) у доменну модель [ReactorState]. */
fun ReactorReadingDto.toDomain(): ReactorState = ReactorState(
    fillLevel = fillLevel,
    methaneConcentration = methaneConcentration,
    temperature = temperature,
    pressure = pressure
)

/** Тіло запиту для створення нового показника на сервері (POST /readings). */
data class CreateReadingRequest(
    val fillLevel: Int,
    val methaneConcentration: Int,
    val temperature: Int,
    val pressure: Int
)

/** Тіло запиту для входу (POST /auth/login) — Заняття №11. */
data class LoginRequest(
    val username: String,
    val password: String
)

/** Відповідь сервера на успішний вхід — містить мокований токен і роль користувача. */
data class LoginResponse(
    val token: String,
    val role: String
)

/** Відповідь захищеного ендпоінту GET /secure/status, доступного лише з токеном. */
data class SecureStatusResponse(
    val user: String,
    val role: String,
    val reactorsOnline: Int,
    val averageMethane: Double,
    val adminNote: String?
)

/** Відповідь нестабільного ендпоінту GET /unstable/forecast — Заняття №12. */
data class UnstableForecastResponse(
    val forecastMethane: Int,
    val note: String
)

/** Типова структура повідомлення про помилку, яку повертає сервер (4xx/5xx). */
data class ApiErrorBody(
    val message: String
)
