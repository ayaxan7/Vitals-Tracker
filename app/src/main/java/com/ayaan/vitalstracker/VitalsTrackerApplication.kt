package com.ayaan.vitalstracker

import android.app.Application
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ayaan.vitalstracker.di.appModule
import com.ayaan.vitalstracker.worker.ReminderWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class VitalsTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin
        startKoin {
            androidContext(this@VitalsTrackerApplication)
            modules(appModule)
        }

        // Schedule periodic reminder work
        scheduleVitalsReminder()
    }

    private fun scheduleVitalsReminder() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .setRequiresCharging(false)
            .setRequiresDeviceIdle(false)
            .setRequiresStorageNotLow(false)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            5, TimeUnit.HOURS
        ).setConstraints(constraints)
            .setInitialDelay(5, TimeUnit.HOURS) // Add initial delay
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE, // Changed from KEEP to REPLACE
            workRequest
        )

        Log.d("VitalsTrackerApplication", "Scheduled vitals reminder with REPLACE policy and 5-hour initial delay")
    }
}
