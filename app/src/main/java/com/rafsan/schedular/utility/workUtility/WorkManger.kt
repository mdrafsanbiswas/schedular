package com.rafsan.schedular.utility.workUtility

import AppLaunchWorker
import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class WorkManagerHelper(val context: Context) {

    private val workManager = WorkManager.getInstance(context)

    fun scheduleAppLaunch(
        packageName: String,
        delayInSeconds: Long,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        val inputData = Data.Builder()
            .putString("packageName", packageName)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<AppLaunchWorker>()
            .setInputData(inputData)
            .setInitialDelay(delayInSeconds, TimeUnit.SECONDS)
            .build()

        workManager.enqueue(workRequest)
        workManager.getWorkInfoByIdLiveData(workRequest.id)
            .observeForever { workInfo ->
                if (workInfo != null) {
                    when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> onSuccess()
                        WorkInfo.State.FAILED -> onFailure()
                        else -> {}
                    }
                }
            }
    }
}