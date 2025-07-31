package com.ayaan.vitalstracker

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.work.*
import com.ayaan.vitalstracker.di.appModule
import com.ayaan.vitalstracker.worker.ReminderWorker
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.FutureCallback
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.UUID
import java.util.concurrent.TimeUnit

class VitalsTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin
        startKoin {
            androidContext(this@VitalsTrackerApplication)
            modules(appModule)
        }

        // Retrieve saved work ID from SharedPreferences
        val sharedPrefs = getSharedPreferences("VitalsPrefs", MODE_PRIVATE)
        val workIdString = sharedPrefs.getString("VitalsWorkId", null)
        val workId = workIdString?.let { UUID.fromString(it) }

        checkAndScheduleVitalsReminder(context = this, workId = workId)
    }

    private fun checkAndScheduleVitalsReminder(context: Context, workId: UUID?) {
        val workManager = WorkManager.getInstance(context)

        if (workId == null) {
            Log.d("VitalsTrackerApplication", "No existing WorkId found. Scheduling new reminder.")
            scheduleVitalsReminder()
            return
        }

        val future = workManager.getWorkInfoById(workId)

        Futures.addCallback(future, object : FutureCallback<WorkInfo> {
            override fun onSuccess(result: WorkInfo?) {
                if (result == null || result.state.isFinished) {
                    Log.d("VitalsTrackerApplication", "WorkInfo is null or finished. Rescheduling.")
                    scheduleVitalsReminder()
                } else {
                    Log.d("VitalsTrackerApplication", "WorkInfo is active (${result.state}). No need to reschedule.")
                }
            }

            override fun onFailure(t: Throwable) {
                Log.e("VitalsTrackerApplication", "Error checking WorkInfo. Scheduling fallback.", t)
                scheduleVitalsReminder()
            }
        }, ContextCompat.getMainExecutor(context))
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
        ).setConstraints(constraints).build()

        val workId = workRequest.id
        val sharedPrefs = getSharedPreferences("VitalsPrefs", MODE_PRIVATE)
        sharedPrefs.edit { putString("VitalsWorkId", workId.toString()) }

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        Log.d("VitalsTrackerApplication", "Scheduled vitals reminder with ID: $workId")
    }
}
