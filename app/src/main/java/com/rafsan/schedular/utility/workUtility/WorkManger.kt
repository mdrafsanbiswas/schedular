package com.rafsan.schedular.utility.workUtility

import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.gson.Gson
import com.rafsan.schedular.data.ScheduleEntity
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerHelper @Inject constructor(
    private val workManager: WorkManager
) {

    fun scheduleAppLaunch(
        data: ScheduleEntity,
        delayInSeconds: Long,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        Log.d("checkTime", "${System.currentTimeMillis()}, $delayInSeconds")
        val duration = delayInSeconds - System.currentTimeMillis()

        val inputData = Data.Builder()
            .putString("package", Gson().toJson(data))
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