package com.example.practicalwork3_variant6.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ReactorReadingEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reactorReadingDao(): ReactorReadingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "biogas_database"
                ).build().also { INSTANCE = it }
            }
    }
}

class LocalBiogasDataSource(private val dao: ReactorReadingDao) {

    fun observeAll() = dao.getAll()

    suspend fun getById(id: Int) = dao.getById(id)

    suspend fun save(reading: ReactorReadingEntity): Long = dao.insert(reading)

    suspend fun saveAll(readings: List<ReactorReadingEntity>) = dao.insertAll(readings)

    suspend fun delete(reading: ReactorReadingEntity) = dao.delete(reading)

    suspend fun clear() = dao.deleteAll()
}
