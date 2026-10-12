package com.example.practicalwork4_variant6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicalwork4_variant6.domain.BiogasStation
import com.example.practicalwork4_variant6.domain.StationDataSource
import com.example.practicalwork4_variant6.domain.StationListProcessor
import com.example.practicalwork4_variant6.domain.StationSortBy
import com.example.practicalwork4_variant6.domain.StationStatus
import com.example.practicalwork4_variant6.domain.StationType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class StationListViewModel : ViewModel() {

    private val _stations = MutableStateFlow(StationDataSource.sampleStations())
    val stations: StateFlow<List<BiogasStation>> = _stations.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortBy = MutableStateFlow(StationSortBy.NAME)
    val sortBy: StateFlow<StationSortBy> = _sortBy.asStateFlow()

    private val _filterType = MutableStateFlow<StationType?>(null)
    val filterType: StateFlow<StationType?> = _filterType.asStateFlow()

    private val _filterStatus = MutableStateFlow<StationStatus?>(null)
    val filterStatus: StateFlow<StationStatus?> = _filterStatus.asStateFlow()

    private val _minPowerKw = MutableStateFlow(0.0)
    val minPowerKw: StateFlow<Double> = _minPowerKw.asStateFlow()

    private val _selectedStationId = MutableStateFlow<Int?>(null)
    val selectedStationId: StateFlow<Int?> = _selectedStationId.asStateFlow()

    @Suppress("UNCHECKED_CAST")
    val processedStations: StateFlow<List<BiogasStation>> = combine(
        _stations, _searchQuery, _filterType, _filterStatus, _minPowerKw, _sortBy
    ) { values ->
        val stations = values[0] as List<BiogasStation>
        val query = values[1] as String
        val type = values[2] as StationType?
        val status = values[3] as StationStatus?
        val minPower = values[4] as Double
        val sortBy = values[5] as StationSortBy
        StationListProcessor.process(stations, query, type, status, minPower, sortBy)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _stations.value
    )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSortBy(sortBy: StationSortBy) {
        _sortBy.value = sortBy
    }

    fun updateFilterType(type: StationType?) {
        _filterType.value = type
    }

    fun updateFilterStatus(status: StationStatus?) {
        _filterStatus.value = status
    }

    fun updateMinPower(minPowerKw: Double) {
        _minPowerKw.value = minPowerKw
    }

    fun selectStation(id: Int?) {
        _selectedStationId.value = if (_selectedStationId.value == id) null else id
    }
}
