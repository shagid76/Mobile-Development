package com.example.practicalwork3_variant6.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BiogasApiService {

    @GET("readings")
    suspend fun getReadings(): Response<List<ReactorReadingDto>>

    @GET("readings/{id}")
    suspend fun getReadingById(@Path("id") id: Int): Response<ReactorReadingDto>

    @POST("readings")
    suspend fun createReading(@Body request: CreateReadingRequest): Response<ReactorReadingDto>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("secure/status")
    suspend fun getSecureStatus(): Response<SecureStatusResponse>

    @GET("unstable/forecast")
    suspend fun getUnstableForecast(): Response<UnstableForecastResponse>
}
