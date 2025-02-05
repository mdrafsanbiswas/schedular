package com.rafsan.schedular.ui.composables

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ManageHistory

import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafsan.schedular.R
import com.rafsan.schedular.data.ScheduleEntity

@Composable
fun HomeScreen(
    apps: List<ScheduleEntity>,
    onCancelClick: (ScheduleEntity) -> Unit,
    onAddClick: () -> Unit = {},
    onHistoryClick: ()-> Unit = {},
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

            if (apps.isEmpty()) {
                EmptyScheduleUI()
            } else {
                Row(modifier = Modifier.fillMaxWidth().height(60.dp), verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(R.drawable.ic_schedule), modifier = Modifier.size(30.dp), contentDescription = null)
                    Spacer(modifier = Modifier.width(15.dp))
                    CommonText("Your schedules", size = 25.sp, modifier = Modifier.weight(1f))
                    CommonIcon(imageVector = Icons.Filled.History, size = 30.dp, onClick = onHistoryClick)
                }
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

@Composable
fun CommonButton(
    text: String,
    onClick: () -> Unit,
    textSize: TextUnit,
    height: Dp = 48.dp,
    paddingValue: Dp = 16.dp,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .then(modifier)
            .padding(paddingValue)
            .height(height)
    ) {
        CommonText(
            text = text,
            fontWeight = FontWeight.Medium,
            size = textSize,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun EmptyScheduleUI() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .wrapContentHeight()
                .background(Color.Red)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_schedule),
                contentDescription = null,
                Modifier.size(222.dp)
            )
            Spacer(modifier = Modifier.height(25.dp))
            CommonText(
                text = stringResource(R.string.no_schedules_are_set),
                fontWeight = FontWeight.Bold,
                size = 25.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            CommonText(
                text = stringResource(R.string.set_a_schedule_to_launch_your_app),
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Medium,
                size = 20.sp,
                color = Color.Gray
            )
        }
    }

}


