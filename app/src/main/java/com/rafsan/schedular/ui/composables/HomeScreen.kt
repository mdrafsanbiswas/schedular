package com.rafsan.schedular.ui.composables

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafsan.schedular.R
import com.rafsan.schedular.data.ScheduleEntity
import com.rafsan.schedular.ui.composables.components.CommonButton
import com.rafsan.schedular.ui.composables.components.CommonIcon
import com.rafsan.schedular.ui.composables.components.CommonText

@Composable
fun HomeScreen(
    apps: List<ScheduleEntity>,
    onCancelClick: (ScheduleEntity) -> Unit,
    onAddClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    appIconMap: Map<String, Drawable>?
) {
    Scaffold(bottomBar = {
        CommonButton(
            text = stringResource(R.string.add_new_schedule),
            onClick = onAddClick,
            textSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
    }, containerColor = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_schedule),
                    modifier = Modifier.size(30.dp),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(15.dp))
                CommonText(
                    stringResource(R.string.your_schedules),
                    size = 25.sp,
                    modifier = Modifier.weight(1f)
                )
                CommonIcon(
                    imageVector = Icons.Filled.History,
                    size = 30.dp,
                    onClick = onHistoryClick
                )
            }

            if (apps.isEmpty()) {
                EmptyScheduleUI()
            } else {
                Spacer(Modifier.height(20.dp))
                ScheduledAppList(
                    apps = apps,
                    onCancelClick = onCancelClick,
                    appIconMap = appIconMap
                )
            }
        }
    }
}


