package com.example.practicalwork3_variant6.data.repository

import com.example.practicalwork3_variant6.data.remote.BiogasApiService
import com.example.practicalwork3_variant6.data.remote.LoginRequest
import com.example.practicalwork3_variant6.data.remote.NetworkResult
import com.example.practicalwork3_variant6.data.remote.SecureStatusResponse
import com.example.practicalwork3_variant6.data.remote.TokenStore
import com.example.practicalwork3_variant6.data.remote.safeApiCall
import kotlinx.coroutines.flow.Flow

/**
 * Repository автентифікації — Заняття №11.
 *
 * Інкапсулює вхід через мокований API, збереження токена в [TokenStore]
 * та звернення до захищеного ресурсу. [com.example.practicalwork3_variant6.data.remote.AuthInterceptor]
 * бере токен із того ж самого [TokenStore] автоматично, тож викликачу
 * (ViewModel) не потрібно самому підставляти заголовок Authorization.
 */
class AuthRepository(
    private val api: BiogasApiService,
    private val tokenStore: TokenStore
) {
    val roleFlow: Flow<String?> = tokenStore.roleFlow
    val tokenFlow: Flow<String?> = tokenStore.tokenFlow

    suspend fun login(username: String, password: String): NetworkResult<String> {
        val result = safeApiCall { api.login(LoginRequest(username, password)) }
        return when (result) {
            is NetworkResult.Success -> {
                tokenStore.save(result.data.token, result.data.role)
                NetworkResult.Success(result.data.role)
            }
            is NetworkResult.Error -> result
            NetworkResult.Loading -> NetworkResult.Loading
        }
    }

    suspend fun logout() {
        tokenStore.clear()
    }

    suspend fun fetchSecureStatus(): NetworkResult<SecureStatusResponse> =
        safeApiCall { api.getSecureStatus() }
}
