package com.example.practicalwork4_variant6

import com.example.practicalwork4_variant6.domain.BiogasStation
import com.example.practicalwork4_variant6.domain.StationListProcessor
import com.example.practicalwork4_variant6.domain.StationSortBy
import com.example.practicalwork4_variant6.domain.StationStatus
import com.example.practicalwork4_variant6.domain.StationType
import org.junit.Assert.assertEquals
import org.junit.Test

class StationListProcessorTest {

    private val stations = listOf(
        BiogasStation(1, "Реактор Альфа", StationType.REACTOR, 42.0, 68, StationStatus.ONLINE, 0L, 50.0, 30.0),
        BiogasStation(2, "Станція Бета", StationType.BIOGAS_PLANT, 128.0, 61, StationStatus.ONLINE, 0L, 50.1, 30.1),
        BiogasStation(3, "Газгольдер Гама", StationType.STORAGE_TANK, 0.0, 55, StationStatus.MAINTENANCE, 0L, 50.2, 30.2),
        BiogasStation(4, "Реактор Дельта", StationType.REACTOR, 18.0, 33, StationStatus.WARNING, 0L, 50.3, 30.3),
    )

    @Test
    fun `search finds stations by case-insensitive substring of the name`() {
        val result = StationListProcessor.search(stations, "реактор")
        assertEquals(listOf("Реактор Альфа", "Реактор Дельта"), result.map { it.name })
    }

    @Test
    fun `search with blank query returns all stations unchanged`() {
        assertEquals(stations, StationListProcessor.search(stations, ""))
    }

    @Test
    fun `search with no matches returns empty list`() {
        assertEquals(emptyList<BiogasStation>(), StationListProcessor.search(stations, "немає такого"))
    }

    @Test
    fun `filterByType keeps only stations of the given type`() {
        val result = StationListProcessor.filterByType(stations, StationType.REACTOR)
        assertEquals(listOf(1, 4), result.map { it.id })
    }

    @Test
    fun `filterByType with null type returns all stations`() {
        assertEquals(stations, StationListProcessor.filterByType(stations, null))
    }

    @Test
    fun `filterByStatus keeps only stations with the given status`() {
        val result = StationListProcessor.filterByStatus(stations, StationStatus.ONLINE)
        assertEquals(listOf(1, 2), result.map { it.id })
    }

    @Test
    fun `filterByMinPower excludes stations below the threshold`() {
        val result = StationListProcessor.filterByMinPower(stations, 40.0)
        assertEquals(listOf(1, 2), result.map { it.id })
    }

    @Test
    fun `sort by name orders alphabetically`() {
        val result = StationListProcessor.sort(stations, StationSortBy.NAME)
        assertEquals(
            listOf("Газгольдер Гама", "Реактор Альфа", "Реактор Дельта", "Станція Бета"),
            result.map { it.name }
        )
    }

    @Test
    fun `sort by power orders from highest to lowest`() {
        val result = StationListProcessor.sort(stations, StationSortBy.POWER)
        assertEquals(listOf(2, 1, 4, 3), result.map { it.id })
    }

    @Test
    fun `process combines search, multiple filters and sorting`() {
        val result = StationListProcessor.process(
            stations = stations,
            query = "реактор",
            type = StationType.REACTOR,
            status = null,
            minPowerKw = 20.0,
            sortBy = StationSortBy.POWER
        )
        assertEquals(listOf("Реактор Альфа"), result.map { it.name })
    }

    @Test
    fun `process with no filters returns all stations sorted by name`() {
        val result = StationListProcessor.process(stations)
        assertEquals(
            listOf("Газгольдер Гама", "Реактор Альфа", "Реактор Дельта", "Станція Бета"),
            result.map { it.name }
        )
    }
}
