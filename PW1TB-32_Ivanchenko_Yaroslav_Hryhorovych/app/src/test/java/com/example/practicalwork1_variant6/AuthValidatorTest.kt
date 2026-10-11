package com.example.practicalwork1_variant6

import com.example.practicalwork1_variant6.domain.AuthValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthValidatorTest {

    @Test
    fun validateLogin_emptyFields_returnsError() {
        val result = AuthValidator.validateLogin("", "")
        assertEquals("Будь ласка, заповніть усі поля", result)
    }

    @Test
    fun validateLogin_validInput_returnsNull() {
        val result = AuthValidator.validateLogin("user", "pass123")
        assertNull(result)
    }

    @Test
    fun validateRegistration_passwordsMismatch_returnsError() {
        val result = AuthValidator.validateRegistration("Ім'я", "a@b.com", "123456", "654321")
        assertEquals("Паролі не співпадають", result)
    }

    @Test
    fun validateRegistration_validInput_returnsNull() {
        val result = AuthValidator.validateRegistration("Ім'я", "a@b.com", "123456", "123456")
        assertNull(result)
    }
}
