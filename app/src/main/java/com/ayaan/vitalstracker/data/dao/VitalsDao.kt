package com.ayaan.vitalstracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ayaan.vitalstracker.data.entity.Vitals
import kotlinx.coroutines.flow.Flow

@Dao
interface VitalsDao {
    @Query("SELECT * FROM vitals ORDER BY timestamp DESC")
    fun getAllVitals(): Flow<List<Vitals>>

    @Insert
    suspend fun insertVitals(vitals: Vitals)

    @Query("SELECT * FROM vitals WHERE id = :id")
    suspend fun getVitalsById(id: Long): Vitals?

    @Query("DELETE FROM vitals WHERE id = :id")
    suspend fun deleteVitals(id: Long)
}
