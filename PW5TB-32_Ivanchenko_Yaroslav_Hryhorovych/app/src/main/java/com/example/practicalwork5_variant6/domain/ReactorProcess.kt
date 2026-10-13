package com.example.practicalwork5_variant6.domain

enum class ReadingType(val label: String, val unit: String) {
    TEMPERATURE("Температура реактора", "°C"),
    GAS_PRESSURE("Тиск біогазу", "кПа"),
    GENERATOR_CURRENT("Струм когенератора", "А"),
    GENERATOR_VOLTAGE("Напруга когенератора", "В")
}
data class ProcessReading(
    val type: ReadingType,
    val value: Float,
    val timestampMs: Long = System.currentTimeMillis()
)

object ReactorThresholds {
    const val TEMPERATURE_MAX_C = 42.0f       // нормальний мезофільний режим бродіння: 35-40°C
    const val GAS_PRESSURE_MAX_KPA = 5.0f     // безпечний робочий тиск газгольдера
    const val GENERATOR_CURRENT_MAX_A = 25.0f

    fun isExceeded(reading: ProcessReading): Boolean = when (reading.type) {
        ReadingType.TEMPERATURE -> reading.value > TEMPERATURE_MAX_C
        ReadingType.GAS_PRESSURE -> reading.value > GAS_PRESSURE_MAX_KPA
        ReadingType.GENERATOR_CURRENT -> reading.value > GENERATOR_CURRENT_MAX_A
        ReadingType.GENERATOR_VOLTAGE -> false
    }
}
