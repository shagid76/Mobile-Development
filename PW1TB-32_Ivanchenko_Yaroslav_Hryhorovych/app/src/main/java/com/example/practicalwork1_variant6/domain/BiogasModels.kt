package com.example.practicalwork1_variant6.domain

/**
 * Поточний стан процесу бродіння в біогазовому реакторі.
 */
data class ReactorState(
    val fillLevel: Int = 0,          // Рівень наповнення реактора, %
    val methaneConcentration: Int = 0, // Концентрація метану CH4, %
    val temperature: Int = 0,        // Температура процесу, °C
    val pressure: Int = 0            // Тиск газу в системі, кПа
)

/**
 * Статус процесу, що визначається на основі концентрації метану.
 * Нормальний робочий діапазон CH4 для біогазу — приблизно 50-70%.
 */
enum class ProcessStatus {
    STABLE,    // Процес у нормі
    WARNING,   // Відхилення від норми
    CRITICAL;  // Критичний стан, потребує втручання

    companion object {
        fun fromMethane(methane: Int): ProcessStatus = when {
            methane in 50..70 -> STABLE
            methane in 30..49 || methane in 71..85 -> WARNING
            else -> CRITICAL
        }
    }
}
