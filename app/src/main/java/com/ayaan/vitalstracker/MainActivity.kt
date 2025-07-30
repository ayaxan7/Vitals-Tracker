package com.ayaan.vitalstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ayaan.vitalstracker.ui.presentation.screens.MainScreen
import com.ayaan.vitalstracker.ui.theme.VitalsTrackerTheme
import com.ayaan.vitalstracker.ui.presentation.viewmodel.VitalsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val vitalsViewModel: VitalsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VitalsTrackerTheme(darkTheme = false) {
                val vitals by vitalsViewModel.vitals.collectAsState()
                val isLoading by vitalsViewModel.isLoading.collectAsState()
                MainScreen(
                    vitals = vitals,
                    isLoading = isLoading,
                    onAddVitals = { systolic, diastolic, heartRate, weight, babyKicks ->
                        vitalsViewModel.insertVitals(
                            systolicPressure = systolic,
                            diastolicPressure = diastolic,
                            heartRate = heartRate,
                            weight = weight,
                            babyKicksCount = babyKicks,

                        )
                    },
                    viewModel = vitalsViewModel
                )
            }
        }
    }
}