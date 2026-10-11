package com.example.practicalwork1_variant6.domain

class BiogasCalculator {
    fun calculateBiogasBasic(massInput: String): Double {
        val mass = massInput.toDoubleOrNull() ?: 0.0
        return mass * 0.35
    }
    fun calculateBiogasMedium(massInput: String, humidityInput: String): Double {
        val mass = massInput.toDoubleOrNull() ?: 0.0
        val humidity = humidityInput.toDoubleOrNull()?.div(100) ?: 0.0
        return mass * 0.35 * (0.5 + humidity)
    }
    fun calculateBiogasAdvanced(
        massInput: String,
        humidityInput: String,
        retentionDaysInput: String
    ): Pair<Double, Double> {
        val mass = massInput.toDoubleOrNull() ?: 0.0
        val humidity = humidityInput.toDoubleOrNull()?.div(100) ?: 0.0
        val days = retentionDaysInput.toDoubleOrNull() ?: 0.0

        val retentionFactor = (days / 20.0).coerceIn(0.0, 1.5)
        val biogasYield = mass * 0.35 * (0.5 + humidity) * (0.5 + retentionFactor)
        val methaneYield = biogasYield * 0.6

        return Pair(biogasYield, methaneYield)
    }
}
