package com.rafsan.schedular.ui

import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafsan.schedular.data.AppInfo
import com.rafsan.schedular.data.ScheduleEntity
import com.rafsan.schedular.repo.ScheduleRepository
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
    private val repository: ScheduleRepository
) : ViewModel() {

    private val _allApps = MutableStateFlow<List<ScheduleEntity>>(emptyList())
    val allApps: StateFlow<List<ScheduleEntity>> = _allApps.asStateFlow()

    private val _showScheduleBottomSheet = MutableStateFlow(false)
    var showScheduleBottomSheet = _showScheduleBottomSheet.asStateFlow()

    private val _showAppListBottomSheet = MutableStateFlow(false)
    var showAppListBottomSheet = _showAppListBottomSheet.asStateFlow()

    private val _showDeleteBottomSheet = MutableStateFlow(false)
    var showDeleteBottomSheet = _showDeleteBottomSheet.asStateFlow()

    private val appIcons = MutableStateFlow<Map<String, Drawable>?>(null)

    fun showAppListBottomSheet() {
        _showAppListBottomSheet.value = true
    }

    fun hideAppListBottomSheet() {
        _showAppListBottomSheet.value = false
    }

    fun showScheduleBottomSheet() {
        _showScheduleBottomSheet.value = true
    }

    fun hideScheduleBottomSheet() {
        _showScheduleBottomSheet.value = false
    }

    private fun getAllApps() {
        viewModelScope.launch {
            repository.getAllApps().collectLatest { apps ->
                _allApps.value = apps
            }
        }
    }

    fun mapAppIcons(data: List<AppInfo>) {
        appIcons.value = data.associate { it.packageName to it.appIcon }
    }

    fun getAppIconMap(): MutableStateFlow<Map<String, Drawable>?> {
        return appIcons
    }

    fun scheduleAppLaunch(data: ScheduleEntity?, delayInSeconds: Long) {
        viewModelScope.launch {
            data?.let { repository.scheduleAppLaunch(it, delayInSeconds) }
        }
    }

    fun deleteSchedule(schedule: ScheduleEntity?) {
        viewModelScope.launch {
            repository.resetSchedule(schedule?.packageName)
        }
    }

    fun formatDate(timestamp: Long): String {
        return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(timestamp))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertAllApps(data: List<AppInfo>) {
        viewModelScope.launch {
            repository.insertAllApps(data)
            getAllApps()
        }
    }

    fun showDeleteBottomSheet() {
        _showDeleteBottomSheet.value = true
    }

    fun hideDeleteBottomSheet() {
        _showDeleteBottomSheet.value = false
    }

    fun deleteRecord(record: ScheduleEntity) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }
}
