package com.ayaan.vitalstracker.ui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayaan.vitalstracker.data.entity.Vitals
import com.ayaan.vitalstracker.data.repository.VitalsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VitalsViewModel(private val repository: VitalsRepository) : ViewModel() {

    private val _vitals = MutableStateFlow<List<Vitals>>(emptyList())
    val vitals: StateFlow<List<Vitals>> = _vitals.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadVitals()
    }

    private fun loadVitals() {
        viewModelScope.launch {
            repository.getAllVitals().collect { vitalsList ->
                _vitals.value = vitalsList
            }
        }
    }

    fun insertVitals(
        systolicPressure: Int,
        diastolicPressure: Int,
        heartRate: Int,
        weight: Float,
        babyKicksCount: Int
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val vitals = Vitals(
                    systolicPressure = systolicPressure,
                    diastolicPressure = diastolicPressure,
                    heartRate = heartRate,
                    weight = weight,
                    babyKicksCount = babyKicksCount
                )
                repository.insertVitals(vitals)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteVitals(id: Long) {
        viewModelScope.launch {
            repository.deleteVitals(id)
        }
    }
}
