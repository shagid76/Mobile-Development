package com.example.practicalwork2_variant6.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reactor_record")
data class ReactorRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fillLevel: Int,
    val methaneConcentration: Int,
    val temperature: Int,
    val pressure: Int,
    val dailyYield: Double,
    val timestamp: Long
)
