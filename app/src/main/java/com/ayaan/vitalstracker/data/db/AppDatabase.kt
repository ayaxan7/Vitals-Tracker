package com.ayaan.vitalstracker.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.ayaan.vitalstracker.data.dao.VitalsDao
import com.ayaan.vitalstracker.data.entity.Vitals

@Database(
    entities = [Vitals::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vitalsDao(): VitalsDao

    companion object {
        const val DATABASE_NAME = "vitals_database"
    }
}
