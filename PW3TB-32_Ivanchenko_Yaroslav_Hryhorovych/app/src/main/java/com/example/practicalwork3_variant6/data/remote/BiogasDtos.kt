package com.example.practicalwork3_variant6.data.remote

import com.example.practicalwork3_variant6.domain.ReactorState

data class ReactorReadingDto(
    val id: Int,
    val fillLevel: Int,
    val methaneConcentration: Int,
    val temperature: Int,
    val pressure: Int,
    val timestamp: Long
)

fun ReactorReadingDto.toDomain(): ReactorState = ReactorState(
    fillLevel = fillLevel,
    methaneConcentration = methaneConcentration,
    temperature = temperature,
    pressure = pressure
)

data class CreateReadingRequest(
    val fillLevel: Int,
    val methaneConcentration: Int,
    val temperature: Int,
    val pressure: Int
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val role: String
)

data class SecureStatusResponse(
    val user: String,
    val role: String,
    val reactorsOnline: Int,
    val averageMethane: Double,
    val adminNote: String?
)

data class UnstableForecastResponse(
    val forecastMethane: Int,
    val note: String
)

data class ApiErrorBody(
    val message: String
)
