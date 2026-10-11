package com.example.practicalwork2_variant6.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReactorRecordDao {

    @Insert
    suspend fun insert(record: ReactorRecord)

    @Delete
    suspend fun delete(record: ReactorRecord)

    @Query("SELECT * FROM reactor_record ORDER BY timestamp DESC")
    fun getAll(): Flow<List<ReactorRecord>>

    @Query("DELETE FROM reactor_record")
    suspend fun deleteAll()
}
