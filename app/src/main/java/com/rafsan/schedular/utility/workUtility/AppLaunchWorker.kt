import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.Data
import com.rafsan.schedular.data.AppDatabase
import com.rafsan.schedular.data.AppInfo
import javax.inject.Inject

class AppLaunchWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val packageName = inputData.getString("packageName") ?: return Result.failure()
        Log.d("AppLaunchWorker", "Attempting to launch app: $packageName")

        val packageManager = applicationContext.packageManager
        try {
            packageManager.getPackageInfo(packageName, 0)
        } catch (e: Exception) {
            Log.e("AppLaunchWorker", "App not installed: $packageName")
            return Result.failure()
        }

        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            notifyUserToLaunchApp(packageName)
            try {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                Log.d("AppLaunchWorker", "Launching app: $packageName")
                applicationContext.startActivity(launchIntent)
            } catch (exception:Exception) {
                Log.e("AppLaunchWorker", "${exception.message}")
            }

            return Result.success()
        } else {
            Log.e("AppLaunchWorker", "Failed to get launch intent for app: $packageName")
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
            .setContentTitle("Launch App 2+${System.currentTimeMillis()/1000}")
            .setContentText("Tap to launch $packageName")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}