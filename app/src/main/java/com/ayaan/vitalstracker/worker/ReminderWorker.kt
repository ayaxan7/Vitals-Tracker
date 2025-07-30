package com.ayaan.vitalstracker.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ayaan.vitalstracker.MainActivity
import com.ayaan.vitalstracker.R

class ReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        var CHANNEL_ID = "vitals_reminder_channel:${System.currentTimeMillis()}"
        var NOTIFICATION_ID =System.currentTimeMillis().toInt()
        var WORK_NAME = "vitals_reminder_work: ${System.currentTimeMillis()}"
    }

    override suspend fun doWork(): Result {
        return try {
            Log.d("ReminderWorker", "Starting notification work")
            createNotificationChannel()
            showNotification()
            Log.d("ReminderWorker", "Notification shown successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e("ReminderWorker", "Failed to show notification", e)
            Result.failure()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Vitals Reminder"
            val descriptionText = "Reminders to log pregnancy vitals"
            val importance = NotificationManager.IMPORTANCE_HIGH // Changed to HIGH for better visibility
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                enableLights(true)
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Time to log your vitals!")
            .setContentText("Stay on top of your health. Please update your vitals now!")
            .setPriority(NotificationCompat.PRIORITY_HIGH) // High priority for better delivery
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Sound, vibration, lights
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
