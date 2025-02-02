package com.rafsan.schedular.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rafsan.schedular.utility.alarmUtility.AlarmScheduler
import com.rafsan.schedular.utility.workUtility.WorkManagerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val workManagerHelper: WorkManagerHelper,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    /*fun scheduleToast(timeMillis: Long, context: Context) {
        alarmScheduler.scheduleExactToast(timeMillis, context)
    }*/

    private val _workStatus = MutableLiveData<String>()
    val workStatus: LiveData<String> get() = _workStatus

    fun scheduleAppLaunch(packageName: String, delayInSeconds: Long) {
        workManagerHelper.scheduleAppLaunch(
            packageName = packageName,
            delayInSeconds = delayInSeconds,
            onSuccess = { _workStatus.value = "Work succeeded!" },
            onFailure = { _workStatus.value = "Work failed!" }
        )
    }
}