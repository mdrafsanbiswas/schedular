package com.rafsan.schedular.ui

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rafsan.schedular.ui.theme.SchedularTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.rafsan.schedular.data.AppInfo
import com.rafsan.schedular.data.ScheduleEntity
import com.rafsan.schedular.ui.composables.ScheduleBottomSheet
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

                InstalledAppsList(
                    installedApps = data,
                    onAppClick = {
                        viewModel.showScheduleBottomSheet()
                        schedule = it
                    },
                    appIconMap = viewModel.getAppIconMap().collectAsState().value
                )

                ScheduleBottomSheet(
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

@Composable
fun InstalledAppsList(
    installedApps: List<ScheduleEntity>,
    onAppClick: (ScheduleEntity) -> Unit,
    appIconMap: Map<String, Drawable>?
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(installedApps) { app ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onAppClick(app) },
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(appIconMap?.get(app.packageName)),
                        contentDescription = app.appName,
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = app.appName)
                }
            }
        }
    }
}



