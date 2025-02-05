package com.rafsan.schedular.ui.composables
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.rafsan.schedular.data.ScheduleEntity
import com.rafsan.schedular.ui.composables.components.CommonIcon
import com.rafsan.schedular.ui.composables.components.CommonText

@Composable
fun InstalledApps(apps: List<ScheduleEntity>, onAppClick: (ScheduleEntity) -> Unit = {}, appIconMap: Map<String, Drawable>?) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(apps) { app ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onAppClick(app) },
            ) {

                AppListItem(
                    app = app,
                    icon = appIconMap?.get(app.packageName),
                )
            }
        }
    }
}

@Composable
fun AppListItem(
    icon: Drawable?,
    app: ScheduleEntity
) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberAsyncImagePainter(icon),
            contentDescription = app.appName,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        CommonText(text = app.appName, fontWeight = FontWeight.Medium, size = 14.sp, modifier = Modifier.weight(1f))

        if(app.isScheduled) {
            CommonIcon(imageVector = Icons.Filled.AccessTime, size = 20.dp)
        }
    }
}
