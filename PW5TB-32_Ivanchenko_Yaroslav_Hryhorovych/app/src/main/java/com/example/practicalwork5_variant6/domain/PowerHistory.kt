package com.example.practicalwork5_variant6.domain

data class PowerPoint(val minuteIndex: Int, val powerKw: Float)

data class ZonePower(val zoneName: String, val powerKw: Float)

object PowerHistoryProcessor {
    fun generateHistory(minutes: Int, seed: Long = 42L): List<PowerPoint> {
        val random = kotlin.random.Random(seed)
        var power = 18f
        return (0 until minutes).map { minute ->
            power = (power + random.nextFloat() * 3f - 1.5f).coerceIn(8f, 30f)
            PowerPoint(minute, power)
        }
    }
    fun windowed(history: List<PowerPoint>, windowMinutes: Int): List<PowerPoint> =
        if (windowMinutes >= history.size) history else history.takeLast(windowMinutes)

    fun average(history: List<PowerPoint>): Float =
        if (history.isEmpty()) 0f else history.sumOf { it.powerKw.toDouble() }.toFloat() / history.size

    fun peak(history: List<PowerPoint>): PowerPoint? = history.maxByOrNull { it.powerKw }

    fun zoneComparison(): List<ZonePower> = listOf(
        ZonePower("Реактор №1", 11.2f),
        ZonePower("Реактор №2", 9.6f),
        ZonePower("Газгольдер", 3.1f),
        ZonePower("Когенератор", 17.8f)
    )
}
