package com.rafsan.schedular.repo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.rafsan.schedular.data.AppDatabase
import com.rafsan.schedular.data.AppInfo
import com.rafsan.schedular.data.ScheduleEntity
import com.rafsan.schedular.utility.workUtility.WorkManagerHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val database: AppDatabase,
    private val workManagerHelper: WorkManagerHelper
) {

    fun getAllApps(): Flow<List<ScheduleEntity>> {
        return database.scheduleDao().getAllApps()
    }

    suspend fun scheduleAppLaunch(data: ScheduleEntity, delayInMillis: Long) {
        if (!isAlreadyScheduled(data.packageName)) {
            database.scheduleDao().scheduleApp(data.packageName, delayInMillis)
            workManagerHelper.scheduleAppLaunch(data, delayInMillis, {}, {})
        } else {
            Log.d("ScheduleRepository", "Package ${data.packageName} is already scheduled.")
        }
    }

    private suspend fun isAlreadyScheduled(packageName: String): Boolean {
        return database.scheduleDao().getScheduledInfo(packageName)?.isScheduled ?: false
    }

    suspend fun resetSchedule(packageName: String?) {
        workManagerHelper.cancelAllWorkByTag(packageName ?: "")
        database.scheduleDao().resetScheduledApp(packageName ?: "")
    }

    suspend fun markAsCompleted(packageName: String, timeInMillis: Long) {
        database.scheduleDao().markScheduleAsComplete(packageName, timeInMillis)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun insertAllApps(data: List<AppInfo>) {
        val appEntities = data.map { app ->
            ScheduleEntity(
                packageName = app.packageName,
                appName = app.appName,
            )
        }
        database.scheduleDao().insertAllApps(appEntities)
    }

    suspend fun deleteRecord(record: ScheduleEntity) {
        database.scheduleDao().resetCompletedRecord(record.packageName)
    }
}