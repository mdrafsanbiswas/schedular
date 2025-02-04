package com.rafsan.schedular.utility.workUtility

import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerHelper  @Inject constructor(
private val workManager: WorkManager
) {

    fun scheduleAppLaunch(
        packageName: String,
        delayInSeconds: Long,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        Log.d("checkTime", "${System.currentTimeMillis()}, $delayInSeconds")
        val duration = delayInSeconds - System.currentTimeMillis()

        val inputData = Data.Builder()
            .putString("packageName", packageName)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<AppLaunchWorker>()
            .setInputData(inputData)
            .setInitialDelay(duration, TimeUnit.MILLISECONDS)
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

    fun cancelAllWorkByTag(packageName: String) {
        workManager.cancelAllWorkByTag(packageName)
    }
}