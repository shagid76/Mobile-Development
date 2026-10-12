package com.example.practicalwork3_variant6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicalwork3_variant6.data.remote.NetworkModule
import com.example.practicalwork3_variant6.data.remote.NetworkResult
import com.example.practicalwork3_variant6.data.remote.SecureStatusResponse
import com.example.practicalwork3_variant6.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** ViewModel для Заняття №11 — автентифікація через мокований токен. */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository(
        api = NetworkModule.provideApiService(application),
        tokenStore = NetworkModule.provideTokenStore(application)
    )

    val role: StateFlow<String?> = repository.roleFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val token: StateFlow<String?> = repository.tokenFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val isLoggedIn: StateFlow<Boolean> = repository.roleFlow
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _loginState = MutableStateFlow<NetworkResult<String>?>(null)
    val loginState: StateFlow<NetworkResult<String>?> = _loginState

    private val _secureStatus = MutableStateFlow<NetworkResult<SecureStatusResponse>?>(null)
    val secureStatus: StateFlow<NetworkResult<SecureStatusResponse>?> = _secureStatus

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = NetworkResult.Error(null, "Логін і пароль не можуть бути порожніми")
            return
        }
        viewModelScope.launch {
            _loginState.value = NetworkResult.Loading
            _loginState.value = repository.login(username, password)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _loginState.value = null
            _secureStatus.value = null
        }
    }

    /** Звернення до захищеного ресурсу — спрацює лише за наявності дійсного токена. */
    fun loadSecureStatus() {
        viewModelScope.launch {
            _secureStatus.value = NetworkResult.Loading
            _secureStatus.value = repository.fetchSecureStatus()
        }
    }
}
