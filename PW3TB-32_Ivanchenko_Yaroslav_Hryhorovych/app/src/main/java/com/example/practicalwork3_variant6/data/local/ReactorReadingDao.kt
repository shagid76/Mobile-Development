package com.example.practicalwork3_variant6.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** DAO (Data Access Object) для локального джерела даних — Заняття №9. */
@Dao
interface ReactorReadingDao {

    @Query("SELECT * FROM reactor_reading ORDER BY timestamp DESC")
    fun getAll(): Flow<List<ReactorReadingEntity>>

    @Query("SELECT * FROM reactor_reading WHERE id = :id")
    suspend fun getById(id: Int): ReactorReadingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reading: ReactorReadingEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(readings: List<ReactorReadingEntity>)

    @Delete
    suspend fun delete(reading: ReactorReadingEntity)

    @Query("DELETE FROM reactor_reading")
    suspend fun deleteAll()
}
