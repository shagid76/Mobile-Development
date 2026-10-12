package com.example.practicalwork4_variant6.domain

enum class StationSortBy {
    NAME, TYPE, POWER, METHANE
}

object StationListProcessor {

    fun search(stations: List<BiogasStation>, query: String): List<BiogasStation> {
        if (query.isBlank()) return stations
        return stations.filter { it.name.contains(query, ignoreCase = true) }
    }

    fun filterByType(stations: List<BiogasStation>, type: StationType?): List<BiogasStation> =
        if (type == null) stations else stations.filter { it.type == type }

    fun filterByStatus(stations: List<BiogasStation>, status: StationStatus?): List<BiogasStation> =
        if (status == null) stations else stations.filter { it.status == status }

    fun filterByMinPower(stations: List<BiogasStation>, minPowerKw: Double): List<BiogasStation> =
        stations.filter { it.powerKw >= minPowerKw }

    fun sort(stations: List<BiogasStation>, by: StationSortBy): List<BiogasStation> = when (by) {
        StationSortBy.NAME -> stations.sortedBy { it.name }
        StationSortBy.TYPE -> stations.sortedBy { it.type.label }
        StationSortBy.POWER -> stations.sortedByDescending { it.powerKw }
        StationSortBy.METHANE -> stations.sortedByDescending { it.methaneLevel }
    }

    fun process(
        stations: List<BiogasStation>,
        query: String = "",
        type: StationType? = null,
        status: StationStatus? = null,
        minPowerKw: Double = 0.0,
        sortBy: StationSortBy = StationSortBy.NAME
    ): List<BiogasStation> {
        var result = search(stations, query)
        result = filterByType(result, type)
        result = filterByStatus(result, status)
        result = filterByMinPower(result, minPowerKw)
        return sort(result, sortBy)
    }
}
