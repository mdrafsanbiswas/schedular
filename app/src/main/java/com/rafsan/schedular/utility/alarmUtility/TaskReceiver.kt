package com.rafsan.schedular.utility.alarmUtility

import AppLaunchWorker
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class TaskReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val workRequest = OneTimeWorkRequestBuilder<AppLaunchWorker>().build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}
