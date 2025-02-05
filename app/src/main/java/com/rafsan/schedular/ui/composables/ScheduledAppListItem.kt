package com.rafsan.schedular.ui.composables

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.rafsan.schedular.R
import com.rafsan.schedular.data.ScheduleEntity
import com.rafsan.schedular.ui.theme.SchedulerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask


@Composable
fun ScheduledAppListItem(
    icon: Drawable,
    onCancel: (ScheduleEntity) -> Unit,
    data: ScheduleEntity
) {
    val context = LocalContext.current
    val scheduleTime = data.scheduleTime?:0L
    val handler = remember { Handler(Looper.getMainLooper()) }
    var relativeTime by remember { mutableStateOf(getReadableRemainingTime(
        scheduleTime - System.currentTimeMillis())
    ) }

    DisposableEffect(data.scheduleTime) {
        val timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                handler.post {
                    relativeTime = getReadableRemainingTime(
                        scheduleTime - System.currentTimeMillis())
                }
            }
        }
        timer.schedule(task, 0, 5000)
        onDispose { timer.cancel() }
    }

    val formattedTime = remember { getFormattedTime(data.scheduleTime?:0L) }

    Row(modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp)).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberAsyncImagePainter(icon),
            contentDescription = "App Icon",
            modifier = Modifier
                .size(40.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            CommonText(text = "In $relativeTime", size = 16.sp, color = MaterialTheme.colorScheme.secondary.copy(alpha = .5f))
            Spacer(modifier = Modifier.height(15.dp))

            CommonText(text = data.appName)
            Spacer(modifier = Modifier.height(6.dp))

            CommonText(text = "\uD83D\uDDD3 $formattedTime", size = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.secondary.copy(alpha = .8f))

        }

        CommonButton(text = stringResource(R.string.cancel), height = 40.dp, onClick = {
            onCancel(data)
        }, textSize = 14.sp, paddingValue = 5.dp)
    }



   /* Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp)).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = icon,
            contentDescription = "App Icon",
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            CommonText(text = appName, color = MaterialTheme.typography.labelMedium.color, size = 20.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(5.dp))
            CommonText(text = packageName, color = MaterialTheme.typography.labelMedium.color, size = MaterialTheme.typography.bodySmall.fontSize, fontWeight = FontWeight.Normal)
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(text = "⏳ $relativeTime", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "📅 $formattedTime",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }*/
}

fun getReadableRemainingTime(millis: Long): String {
    val seconds = millis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 0 -> "$days day${if (days > 1) "s" else ""}"
        hours > 0 -> "$hours hour${if (hours > 1) "s" else ""}"
        minutes > 0 -> "$minutes min${if (minutes > 1) "s" else ""}"
        else -> "$seconds sec${if (seconds > 1) "s" else ""}"
    }
}


fun getRelativeTime(targetMillis: Long, context: Context): String {
    val now = System.currentTimeMillis()
    return DateUtils.getRelativeTimeSpanString(
        targetMillis,
        now,
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_RELATIVE
    ).toString()
}

fun getFormattedTime(timeMillis: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy, h:mm a", Locale.getDefault())
    return sdf.format(Date(timeMillis))
}

