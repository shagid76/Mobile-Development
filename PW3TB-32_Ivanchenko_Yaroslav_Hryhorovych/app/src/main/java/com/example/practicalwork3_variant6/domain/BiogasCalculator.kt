package com.example.practicalwork3_variant6.domain

/**
 * Перенесено з Практичної роботи №1 без змін — розрахунок виходу біогазу
 * та метану за масою сировини, вологістю та часом бродіння.
 */
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

    /**
     * Спрощений розрахунок виходу біогазу (м³/добу) безпосередньо зі стану
     * реактора — використовується у заняттях 6-8, де вхідними даними є
     * параметри реактора (наповнення, метан, температура, тиск), а не маса
     * сировини, як у ПР1.
     */
    fun estimateDailyYield(state: ReactorState): Double {
        val fillFactor = state.fillLevel / 100.0
        val methaneFactor = state.methaneConcentration / 100.0
        val temperatureFactor = (state.temperature / 35.0).coerceIn(0.2, 1.3)
        return (fillFactor * methaneFactor * temperatureFactor * 10.0)
            .coerceAtLeast(0.0)
    }
}
