package com.ayaan.vitalstracker

import android.app.Application
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
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

        val reminderWork = PeriodicWorkRequestBuilder<ReminderWorker>(
            repeatInterval = 5,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .addTag("vitals_reminder")
            .build()

        Log.d("VitalsTrackerApplication", "workReminderOnjct: $reminderWork")


        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            reminderWork
        )
    }
}
