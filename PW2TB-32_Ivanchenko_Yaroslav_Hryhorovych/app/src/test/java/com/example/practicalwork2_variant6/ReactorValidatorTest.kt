package com.example.practicalwork2_variant6

import com.example.practicalwork2_variant6.domain.ReactorValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ReactorValidatorTest {

    @Test
    fun validateFillLevel_empty_returnsError() {
        val result = ReactorValidator.validateFillLevel("")
        assertEquals("Поле «Рівень наповнення» є обов'язковим", result)
    }

    @Test
    fun validateFillLevel_notNumber_returnsError() {
        val result = ReactorValidator.validateFillLevel("abc")
        assertEquals("«Рівень наповнення» має бути цілим числом", result)
    }

    @Test
    fun validateFillLevel_outOfRange_returnsError() {
        val result = ReactorValidator.validateFillLevel("150")
        assertEquals("Рівень наповнення має бути в межах 0–100%", result)
    }

    @Test
    fun validateFillLevel_validInput_returnsNull() {
        assertNull(ReactorValidator.validateFillLevel("60"))
    }

    @Test
    fun validateMethane_validInput_returnsNull() {
        assertNull(ReactorValidator.validateMethane("55"))
    }

    @Test
    fun validateTemperature_outOfRange_returnsError() {
        assertEquals("Температура має бути в межах -10…80 °C", ReactorValidator.validateTemperature("150"))
    }

    @Test
    fun validatePressure_outOfRange_returnsError() {
        assertEquals("Тиск має бути в межах 0–500 кПа", ReactorValidator.validatePressure("600"))
    }

    @Test
    fun validateReactorForm_allValid_returnsEmptyMap() {
        val errors = ReactorValidator.validateReactorForm("60", "55", "36", "120")
        assertTrue(errors.isEmpty())
    }

    @Test
    fun validateReactorForm_someInvalid_returnsErrorsForThoseFields() {
        val errors = ReactorValidator.validateReactorForm("150", "55", "36", "120")
        assertEquals(1, errors.size)
        assertTrue(errors.containsKey("fillLevel"))
    }

    @Test
    fun crossFieldWarning_highFillLowMethane_returnsWarning() {
        val warning = ReactorValidator.crossFieldWarning(fillLevel = 98, methane = 20)
        assertEquals(
            "Увага: реактор майже заповнений, а метану мало — перевірте процес бродіння",
            warning
        )
    }

    @Test
    fun crossFieldWarning_normalValues_returnsNull() {
        assertNull(ReactorValidator.crossFieldWarning(fillLevel = 60, methane = 55))
    }
}
