package com.rafsan.schedular.ui.composables

import android.content.Context
import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ScheduledAppListItem(
    icon: Painter,
    appName: String,
    packageName: String,
    scheduledTimeMillis: Long,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val relativeTime = remember { getRelativeTime(scheduledTimeMillis, context) }
    val formattedTime = remember { getFormattedTime(scheduledTimeMillis) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
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
            Text(text = appName, style = MaterialTheme.typography.titleMedium)
            Text(
                text = packageName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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

