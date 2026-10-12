package com.example.practicalwork4_variant6.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

object StationDataSource {

    fun sampleStations(): List<BiogasStation> = listOf(
        BiogasStation(1, "Реактор №1 «Південний»", StationType.REACTOR, 42.5, 68, StationStatus.ONLINE, baseTime, 50.4012, 30.5698),
        BiogasStation(2, "Станція «Бориспільська»", StationType.BIOGAS_PLANT, 128.3, 61, StationStatus.ONLINE, baseTime, 50.3498, 30.9550),
        BiogasStation(3, "Газгольдер №2", StationType.STORAGE_TANK, 0.0, 55, StationStatus.MAINTENANCE, baseTime, 50.3892, 30.6102),
        BiogasStation(4, "Когенерація «Вишгород»", StationType.COGENERATION_UNIT, 215.7, 72, StationStatus.ONLINE, baseTime, 50.5820, 30.4890),
        BiogasStation(5, "Реактор №3 «Східний»", StationType.REACTOR, 18.9, 33, StationStatus.WARNING, baseTime, 50.4550, 30.6820),
        BiogasStation(6, "Станція «Васильківська»", StationType.BIOGAS_PLANT, 96.4, 58, StationStatus.ONLINE, baseTime, 50.1830, 30.3230),
        BiogasStation(7, "Газгольдер №5", StationType.STORAGE_TANK, 0.0, 12, StationStatus.OFFLINE, baseTime, 50.4210, 30.5120),
        BiogasStation(8, "Когенерація «Броварська»", StationType.COGENERATION_UNIT, 174.2, 65, StationStatus.ONLINE, baseTime, 50.5130, 30.7900),
    )

    private val baseTime: Long get() = System.currentTimeMillis()

    fun sensorFlow(emissions: Int = 6): Flow<Int> = flow {
        var methane = 55
        repeat(emissions) {
            delay(900)
            methane = (methane + Random.nextInt(-6, 7)).coerceIn(10, 95)
            emit(methane)
        }
    }

    fun forecastFlow(emissions: Int = 6): Flow<Double> = flow {
        var power = 80.0
        repeat(emissions) {
            delay(1200)
            if (Random.nextInt(0, 5) == 0) {
                throw IllegalStateException("Прогнозна модель тимчасово недоступна")
            }
            power = (power + Random.nextDouble(-15.0, 15.0)).coerceIn(0.0, 220.0)
            emit(power)
        }
    }
}
