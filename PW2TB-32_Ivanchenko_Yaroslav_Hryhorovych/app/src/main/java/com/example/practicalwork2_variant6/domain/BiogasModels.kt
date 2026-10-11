package com.example.practicalwork2_variant6.domain

data class ReactorState(
    val fillLevel: Int = 0,
    val methaneConcentration: Int = 0,
    val temperature: Int = 0,
    val pressure: Int = 0
)

enum class ProcessStatus {
    STABLE,
    WARNING,
    CRITICAL;

    companion object {
        fun fromMethane(methane: Int): ProcessStatus = when {
            methane in 50..70 -> STABLE
            methane in 30..49 || methane in 71..85 -> WARNING
            else -> CRITICAL
        }
    }
}

fun ProcessStatus.label(): String = when (this) {
    ProcessStatus.STABLE -> "Стабільний"
    ProcessStatus.WARNING -> "Потребує уваги"
    ProcessStatus.CRITICAL -> "Критичний"
}
