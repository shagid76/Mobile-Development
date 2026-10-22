package com.example.practicalwork6_variant6.domain

enum class ResourceStatus { NORMAL, WARNING, CRITICAL }

object ResourceEvaluator {

    fun classify(percent: Float): ResourceStatus = when {
        percent < 0f || percent.isNaN() -> ResourceStatus.NORMAL
        percent >= 90f -> ResourceStatus.CRITICAL
        percent >= 70f -> ResourceStatus.WARNING
        else -> ResourceStatus.NORMAL
    }

    fun isBatteryLow(batteryPercent: Int): Boolean = batteryPercent in 0..15

    fun memoryUsagePercent(totalBytes: Long, availableBytes: Long): Float {
        if (totalBytes <= 0L || availableBytes < 0L) return 0f
        val used = (totalBytes - availableBytes).coerceAtLeast(0L)
        return (used.toDouble() / totalBytes.toDouble() * 100.0).toFloat().coerceIn(0f, 100f)
    }
}
