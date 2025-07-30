package com.ayaan.vitalstracker.data.repository

import com.ayaan.vitalstracker.data.dao.VitalsDao
import com.ayaan.vitalstracker.data.entity.Vitals
import kotlinx.coroutines.flow.Flow

class VitalsRepository(private val vitalsDao: VitalsDao) {

    fun getAllVitals(): Flow<List<Vitals>> = vitalsDao.getAllVitals()

    suspend fun insertVitals(vitals: Vitals) = vitalsDao.insertVitals(vitals)

    suspend fun getVitalsById(id: Long): Vitals? = vitalsDao.getVitalsById(id)

    suspend fun deleteVitals(id: Long) = vitalsDao.deleteVitals(id)
}
