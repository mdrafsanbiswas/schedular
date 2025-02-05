package com.rafsan.schedular.ui.composables

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rafsan.schedular.R
import com.rafsan.schedular.data.ScheduleEntity

@Composable
fun ScheduledAppList(
    apps: List<ScheduleEntity>,
    onCancelClick: (ScheduleEntity) -> Unit,
    appIconMap: Map<String, Drawable>?
) {
    LazyColumn(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        items(apps) {
            appIconMap?.get(it.packageName)?.let { icon ->
                ScheduledAppListItem(
                    icon = icon,
                    data = it,
                    onCancel = onCancelClick
                )
            }
        }
    }

}