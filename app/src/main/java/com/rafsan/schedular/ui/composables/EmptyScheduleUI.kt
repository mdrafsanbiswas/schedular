package com.rafsan.schedular.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafsan.schedular.R
import com.rafsan.schedular.ui.composables.components.CommonText

@Composable
fun EmptyScheduleUI(
    title: String = stringResource(R.string.no_schedules_are_set),
    subTitle: String = stringResource(R.string.set_a_schedule_to_launch_your_app)
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_schedule),
            contentDescription = null,
            modifier = Modifier.size(222.dp)
        )
        Spacer(modifier = Modifier.height(25.dp))
        CommonText(
            text = title,
            fontWeight = FontWeight.Bold,
            size = 25.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        CommonText(
            text = subTitle,
            fontWeight = FontWeight.Medium,
            size = 20.sp,
            color = Color.Gray
        )
    }
}