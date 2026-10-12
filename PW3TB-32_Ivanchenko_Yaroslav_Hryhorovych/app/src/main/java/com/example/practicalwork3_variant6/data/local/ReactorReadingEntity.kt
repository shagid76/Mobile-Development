package com.example.practicalwork3_variant6.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.practicalwork3_variant6.domain.ReactorState

@Entity(tableName = "reactor_reading")
data class ReactorReadingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fillLevel: Int,
    val methaneConcentration: Int,
    val temperature: Int,
    val pressure: Int,
    val timestamp: Long,
    val isSynced: Boolean = false
)

fun ReactorReadingEntity.toDomain(): ReactorState = ReactorState(
    fillLevel = fillLevel,
    methaneConcentration = methaneConcentration,
    temperature = temperature,
    pressure = pressure
)
