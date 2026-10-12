package com.example.practicalwork4_variant6.domain

data class BiogasStation(
    val id: Int,
    val name: String,
    val type: StationType,
    val powerKw: Double,            // поточна потужність генерації, кВт
    val methaneLevel: Int,          // концентрація метану CH4, %
    val status: StationStatus,
    val lastUpdateEpochMillis: Long,
    val latitude: Double,
    val longitude: Double
)

enum class StationType(val label: String) {
    REACTOR("Реактор бродіння"),
    BIOGAS_PLANT("Біогазова станція"),
    STORAGE_TANK("Газгольдер (сховище)"),
    COGENERATION_UNIT("Когенераційна установка")
}

enum class StationStatus(val label: String) {
    ONLINE("У роботі"),
    WARNING("Потребує уваги"),
    OFFLINE("Відключено"),
    MAINTENANCE("На обслуговуванні")
}
