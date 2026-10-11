package com.example.practicalwork2_variant6.domain

object ReactorValidator {

    fun validateRequiredInt(label: String, value: String): String? {
        if (value.isBlank()) return "Поле «$label» є обов'язковим"
        if (value.toIntOrNull() == null) return "«$label» має бути цілим числом"
        return null
    }

    fun validateFillLevel(value: String): String? {
        validateRequiredInt("Рівень наповнення", value)?.let { return it }
        val level = value.toInt()
        return if (level !in 0..100) "Рівень наповнення має бути в межах 0–100%" else null
    }

    fun validateMethane(value: String): String? {
        validateRequiredInt("Концентрація метану", value)?.let { return it }
        val methane = value.toInt()
        return if (methane !in 0..100) "Концентрація метану має бути в межах 0–100%" else null
    }

    fun validateTemperature(value: String): String? {
        validateRequiredInt("Температура", value)?.let { return it }
        val temperature = value.toInt()
        return if (temperature !in -10..80) "Температура має бути в межах -10…80 °C" else null
    }

    fun validatePressure(value: String): String? {
        validateRequiredInt("Тиск", value)?.let { return it }
        val pressure = value.toInt()
        return if (pressure !in 0..500) "Тиск має бути в межах 0–500 кПа" else null
    }

    fun validateReactorForm(
        fillLevel: String,
        methane: String,
        temperature: String,
        pressure: String
    ): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        validateFillLevel(fillLevel)?.let { errors["fillLevel"] = it }
        validateMethane(methane)?.let { errors["methane"] = it }
        validateTemperature(temperature)?.let { errors["temperature"] = it }
        validatePressure(pressure)?.let { errors["pressure"] = it }
        return errors
    }

    fun crossFieldWarning(fillLevel: Int, methane: Int): String? {
        return if (fillLevel > 95 && methane < 40) {
            "Увага: реактор майже заповнений, а метану мало — перевірте процес бродіння"
        } else null
    }
}
