package com.example.practicalwork4_variant6.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class UserLocation(val latitude: Double, val longitude: Double)

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _userLocation = MutableStateFlow<UserLocation?>(null)
    val userLocation: StateFlow<UserLocation?> = _userLocation.asStateFlow()

    private val _statusMessage = MutableStateFlow("Місцезнаходження ще не визначено")
    val statusMessage: StateFlow<String> = _statusMessage.asStateFlow()

    @SuppressLint("MissingPermission")
    fun refreshLocation() {
        viewModelScope.launch {
            try {
                val location = fusedLocationClient.lastLocation.await()
                if (location != null) {
                    _userLocation.value = UserLocation(location.latitude, location.longitude)
                    _statusMessage.value = "Місцезнаходження визначено"
                } else {
                    _statusMessage.value = "Дані GPS відсутні — увімкніть геолокацію на пристрої"
                }
            } catch (e: Exception) {
                _statusMessage.value = "Не вдалося отримати місцезнаходження: ${e.message}"
            }
        }
    }

    fun onPermissionDenied() {
        _statusMessage.value = "Доступ до геолокації відхилено користувачем"
    }
}
