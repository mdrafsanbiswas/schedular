package com.rafsan.schedular.ui

import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafsan.schedular.data.AppDatabase
import com.rafsan.schedular.data.AppInfo
import com.rafsan.schedular.data.Converters
import com.rafsan.schedular.data.ScheduleEntity
import com.rafsan.schedular.utility.workUtility.WorkManagerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val workManagerHelper: WorkManagerHelper,
    private val database: AppDatabase,
) : ViewModel() {

    private val _allApps = MutableStateFlow<List<ScheduleEntity>>(emptyList())
    val allApps: StateFlow<List<ScheduleEntity>> = _allApps.asStateFlow()

    private val appIcons = MutableStateFlow<Map<String, Drawable>?>(null)

    private fun getAllApps() {
        viewModelScope.launch {
            viewModelScope.launch {
                database.scheduleDao().getAllApps().collectLatest { apps ->
                    _allApps.value = apps
                }
            }
        }
    }

    fun mapAppIcons(data: List<AppInfo>) {
        data.forEach {
            appIcons.value = mapOf(
                it.packageName to it.appIcon
            )
        }
    }

    fun scheduleAppLaunch(packageName: String, delayInSeconds: Long) {

        viewModelScope.launch {
            if (!isAlreadyScheduled(packageName)) {
                database.scheduleDao().scheduleApp(packageName)
                workManagerHelper.scheduleAppLaunch(packageName, delayInSeconds, {}, {})
            } else {
                Log.d("scheduleAppLaunch", "Package $packageName is already scheduled.")
            }
        }
    }

    private suspend fun isAlreadyScheduled(packageName: String) : Boolean {
       return database.scheduleDao().getScheduledInfo(packageName)?.isScheduled?:false
    }

    fun deleteSchedule(schedule: ScheduleEntity) {
        workManagerHelper.cancelAllWorkByTag(schedule.packageName)
        viewModelScope.launch { database.scheduleDao().deleteScheduledApp(schedule) }
    }

    fun formatDate(timestamp: Long): String {
        return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(timestamp))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertAllApps(data: List<AppInfo>) {
        viewModelScope.launch {
            val appEntities = data.map { app ->
                ScheduleEntity(
                    packageName = app.packageName,
                    appName = app.appName,
                   // appIcon = Converters().fromDrawable(app.appIcon) ?: ByteArray(0)
                )
            }
            database.scheduleDao().insertAllApps(appEntities)
            getAllApps()
        }
    }

}

