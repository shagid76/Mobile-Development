package com.example.practicalwork1_variant6.domain

object AuthValidator {

    fun validateLogin(login: String, password: String): String? {
        if (login.isBlank() || password.isBlank()) {
            return "Будь ласка, заповніть усі поля"
        }
        return null
    }

    fun validateRegistration(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): String? {
        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            return "Будь ласка, заповніть усі поля"
        }
        if (!email.contains("@")) {
            return "Некоректна електронна пошта"
        }
        if (password.length < 6) {
            return "Пароль має містити щонайменше 6 символів"
        }
        if (password != confirmPassword) {
            return "Паролі не співпадають"
        }
        return null
    }
}
