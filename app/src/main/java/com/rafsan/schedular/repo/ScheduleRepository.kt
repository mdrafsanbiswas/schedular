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

    suspend fun scheduleAppLaunch(packageName: String, delayInMillis: Long) {
        if (!isAlreadyScheduled(packageName)) {
            database.scheduleDao().scheduleApp(packageName, delayInMillis)
            workManagerHelper.scheduleAppLaunch(packageName, delayInMillis, {}, {})
        } else {
            Log.d("ScheduleRepository", "Package $packageName is already scheduled.")
        }
    }

    private suspend fun isAlreadyScheduled(packageName: String): Boolean {
        return database.scheduleDao().getScheduledInfo(packageName)?.isScheduled ?: false
    }

    suspend fun deleteSchedule(schedule: ScheduleEntity?) {
        workManagerHelper.cancelAllWorkByTag(schedule?.packageName?:"")
        schedule?.let { database.scheduleDao().resetScheduledApp(schedule.packageName) }
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
}