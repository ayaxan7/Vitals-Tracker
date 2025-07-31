package com.ayaan.vitalstracker

import android.app.Application
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.*
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

        checkAndScheduleVitalsReminder()
    }

    private fun checkAndScheduleVitalsReminder() {
        val workManager = WorkManager.getInstance(this)

        workManager.getWorkInfosForUniqueWork(ReminderWorker.WORK_NAME)
            .addListener({
                val workInfos = workManager.getWorkInfosForUniqueWork(ReminderWorker.WORK_NAME).get()

                val isAlreadyScheduled = workInfos.any {
                    it.state == WorkInfo.State.ENQUEUED || it.state == WorkInfo.State.RUNNING
                }
                Log.d("VitalsTrackerApplication", "WorkInfos for ${ReminderWorker.WORK_NAME}: $workInfos is scheduled: $isAlreadyScheduled")
                if (isAlreadyScheduled) {
                    Log.d("VitalsTrackerApplication", "Vitals reminder already scheduled or running. Skipping.")
                } else {
                    Log.d("VitalsTrackerApplication", "Not scheduled yet. Proceeding to schedule.")
                    scheduleVitalsReminder()
                }

            }, ContextCompat.getMainExecutor(this))
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
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        Log.d("VitalsTrackerApplication", "Scheduled vitals reminder.")
    }
}
