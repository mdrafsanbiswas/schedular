package com.rafsan.schedular.ui

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.rafsan.schedular.ui.theme.SchedularTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rafsan.schedular.data.AppInfo
import com.rafsan.schedular.data.ScheduleEntity
import com.rafsan.schedular.ui.composables.AppListBottomSheet
import com.rafsan.schedular.ui.composables.DateAndTimePickerDialog
import com.rafsan.schedular.ui.composables.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel : ScheduleViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        val apps = getInstalledApps(this)

        viewModel.insertAllApps(data = apps)
        viewModel.mapAppIcons(data = apps)

        setContent {

            val data = viewModel.allApps.collectAsState().value
            var schedule: ScheduleEntity? by remember { mutableStateOf(null) }

            SchedularTheme {

                HomeScreen(
                    apps = data.filter { it.isScheduled },
                    onAddClick = {
                       viewModel.showAppListBottomSheet()
                   },
                    onCancelClick = {
                        viewModel.deleteSchedule(it)
                    }
                )

                AppListBottomSheet(
                    state = viewModel.showAppListBottomSheet.collectAsState().value,
                    apps = data,
                    onDismiss = {
                        viewModel.hideAppListBottomSheet()
                    },
                    appIconMap = viewModel.getAppIconMap().collectAsState().value,
                    onAppClick = {
                        schedule = it
                        viewModel.hideAppListBottomSheet()
                        viewModel.showScheduleBottomSheet()
                    }
                )

                /*ScheduleBottomSheet(
                    state = viewModel.showScheduleBottomSheet.collectAsState().value,
                    schedule = schedule,
                    onDismiss = {
                        viewModel.hideScheduleBottomSheet()
                    },
                    onCancelSchedule = {
                        viewModel.deleteSchedule(schedule)
                    },
                    onSetSchedule = {
                        Log.d("schedule_time", "$it")
                        viewModel.scheduleAppLaunch(
                            schedule?.packageName?:"",
                            it
                        )
                    }
                )*/

                DateAndTimePickerDialog(
                    state = viewModel.showScheduleBottomSheet.collectAsState().value,
                    onDismiss = {
                        viewModel.hideScheduleBottomSheet()
                    },
                    onSetSchedule = {
                        viewModel.scheduleAppLaunch(
                            schedule?.packageName?:"",
                            it
                        )
                    }
                )
            }
        }
    }

    private fun getInstalledApps(context: Context): List<AppInfo> {
        val pm = context.packageManager
        val apps = mutableListOf<AppInfo>()

        val installedPackages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        for (packageInfo in installedPackages) {
            if ((packageInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                val appName = packageInfo.loadLabel(pm).toString()
                val appIcon = packageInfo.loadIcon(pm)

                apps.add(AppInfo(appName, packageInfo.packageName, appIcon))
            }
        }
        return apps
    }
}





