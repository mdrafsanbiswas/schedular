package com.rafsan.schedular.utility.workUtility

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rafsan.schedular.repo.ScheduleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AppLaunchWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ScheduleRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val packageName = inputData.getString("packageName") ?: return Result.failure()

        val packageManager = applicationContext.packageManager
        try {
            packageManager.getPackageInfo(packageName, 0)
        } catch (e: Exception) {
            return Result.failure()
        }

        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            notifyUserToLaunchApp(packageName)
            try {
                repository.resetSchedule(packageName)

                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                applicationContext.startActivity(launchIntent)
            } catch (exception: Exception) {
                Log.e("AppLaunchWorker", "${exception.message}")
                return Result.failure()
            }

            return Result.success()
        } else {
            notifyUserToLaunchApp(packageName)
            return Result.failure()
        }
    }

    private fun notifyUserToLaunchApp(packageName: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "app_launch_channel",
                "App Launch Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val launchIntent = applicationContext.packageManager.getLaunchIntentForPackage(packageName)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, "app_launch_channel")
            .setContentTitle("Launch App")
            .setContentText("Tap to launch $packageName")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}