package com.rafsan.schedular.ui.composables

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoDelete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.rafsan.schedular.R
import com.rafsan.schedular.data.ScheduleEntity
import com.rafsan.schedular.ui.ScheduleViewModel
import com.rafsan.schedular.ui.composables.components.CommonButton
import com.rafsan.schedular.ui.composables.components.CommonIcon
import com.rafsan.schedular.ui.composables.components.CommonText

@Composable
fun CompletedSchedulesScreen(
    viewModel: ScheduleViewModel,
    onBackClick: () -> Boolean,
    onDeleteRecord: (ScheduleEntity) -> Unit
) {

    val apps = viewModel.allApps.collectAsState().value.filter { it.completed }
    val appIconMap = viewModel.getAppIconMap().collectAsState().value

    Scaffold(containerColor = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .padding()
                    .height(56.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {
                            onBackClick()
                        })
                CommonText(
                    stringResource(R.string.completed_schedules),
                    size = 20.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (apps.isEmpty()) {
                EmptyScheduleUI(
                    title = stringResource(R.string.no_completed_schedules_yet),
                    subTitle = stringResource(
                        R.string.completed_schedules_will_be_here
                    )
                )
            } else {
                Spacer(modifier = Modifier.height(20.dp))
                CompletedScheduleList(
                    apps = apps,
                    appIconMap = appIconMap,
                    onDeleteRecord = onDeleteRecord
                )
            }
        }
    }
}

@Composable
fun CompletedScheduleList(
    apps: List<ScheduleEntity>,
    appIconMap: Map<String, Drawable>?,
    onDeleteRecord: (ScheduleEntity) -> Unit = {}
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        items(apps) {
            appIconMap?.get(it.packageName)?.let { icon ->
                CompletedScheduledListItem(
                    icon = icon,
                    data = it,
                    onDelete = onDeleteRecord
                )
            }
        }
    }
}

@Composable
fun CompletedScheduledListItem(
    icon: Drawable,
    data: ScheduleEntity,
    onDelete: (ScheduleEntity) -> Unit
) {

    val formattedTime = remember { getFormattedTime(data.completionTime ?: 0L) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(icon),
            contentDescription = "App Icon",
            modifier = Modifier
                .size(35.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {

            CommonText(text = data.appName, size = 18.sp)
            Spacer(modifier = Modifier.height(6.dp))

            CommonText(
                text = "\uD83D\uDDD3 $formattedTime",
                size = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = .8f)
            )

        }

        CommonIcon(imageVector = Icons.Filled.AutoDelete, size = 25.dp, onClick = {
            onDelete(data)
        })
    }
}
