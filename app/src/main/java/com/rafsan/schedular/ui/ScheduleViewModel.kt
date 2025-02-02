package com.rafsan.schedular.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.rafsan.schedular.data.AppDatabase
import com.rafsan.schedular.data.ScheduleDao
import com.rafsan.schedular.data.ScheduleEntity
import com.rafsan.schedular.utility.alarmUtility.AlarmScheduler
import com.rafsan.schedular.utility.workUtility.WorkManagerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.truncate

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val workManagerHelper: WorkManagerHelper,
    private val database: AppDatabase,
) : ViewModel() {

   // val completedSchedules = database.scheduleDao().getAllCompletedSchedules()

    fun scheduleAppLaunch(packageName: String, delayInSeconds: Long) {
        viewModelScope.launch {
            val schedule = ScheduleEntity(packageName = packageName, scheduleTime = System.currentTimeMillis() + delayInSeconds * 1000)
                database.scheduleDao().insertSchedule(schedule)
                database.scheduleDao().getAllSchedules().collectLatest {
                    Log.d("data_room", "$it")
                }
        }
        workManagerHelper.scheduleAppLaunch(packageName, delayInSeconds, {}, {})
    }

    fun cancelSchedule(schedule: ScheduleEntity) {
        workManagerHelper.cancelAllWorkByTag(schedule.packageName)
        viewModelScope.launch { database.scheduleDao().deleteSchedule(schedule) }
    }

    fun formatDate(timestamp: Long): String {
        return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(timestamp))
    }
}

