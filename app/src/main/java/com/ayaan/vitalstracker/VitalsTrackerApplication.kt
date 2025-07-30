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
            5, TimeUnit.HOURS // Set to 5 hours for production
        ).setConstraints(constraints).build()

        // Use KEEP policy to prevent rescheduling if work already exists
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // This prevents immediate execution on app restart
            workRequest
        )

        Log.d("VitalsTrackerApplication", "Scheduled vitals reminder with KEEP policy")
    }
}
