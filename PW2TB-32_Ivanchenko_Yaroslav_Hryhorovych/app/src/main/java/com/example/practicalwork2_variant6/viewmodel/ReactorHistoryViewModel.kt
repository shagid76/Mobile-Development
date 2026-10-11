package com.example.practicalwork2_variant6.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicalwork2_variant6.data.AppDatabase
import com.example.practicalwork2_variant6.data.ReactorRecord
import com.example.practicalwork2_variant6.domain.BiogasCalculator
import com.example.practicalwork2_variant6.domain.ReactorState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReactorHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).reactorRecordDao()
    private val calculator = BiogasCalculator()

    val records: StateFlow<List<ReactorRecord>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveRecord(state: ReactorState) {
        viewModelScope.launch {
            dao.insert(
                ReactorRecord(
                    fillLevel = state.fillLevel,
                    methaneConcentration = state.methaneConcentration,
                    temperature = state.temperature,
                    pressure = state.pressure,
                    dailyYield = calculator.estimateDailyYield(state),
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteRecord(record: ReactorRecord) {
        viewModelScope.launch { dao.delete(record) }
    }

    fun clearAll() {
        viewModelScope.launch { dao.deleteAll() }
    }
}
