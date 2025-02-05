package com.rafsan.schedular.ui.composables
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    apps: List<ScheduleEntity>,
    onCancelClick: (ScheduleEntity) -> Unit,
    onAddClick: ()-> Unit = {}
) {
    Scaffold(bottomBar = {
        CommonButton(text = stringResource(R.string.add_new_schedule), onClick = onAddClick, textSize = 15.sp)
    }) {
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(start = 25.dp, end = 25.dp, top = 25.dp)
            .padding(it)) {

            if (apps.isEmpty()) {
                EmptyScheduleUI()
            } else {
                ScheduledAppList(
                    apps = apps,
                    onCancelClick = onCancelClick
                )
            }
        }
    }
}

@Composable
fun CommonButton(text: String, onClick: () -> Unit, textSize: TextUnit, height: Dp = 48.dp) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(height)
    ) {
        CommonText(text = text, fontWeight = FontWeight.Medium, size = textSize, color = MaterialTheme.typography.labelMedium.color)
    }
}

@Composable
fun EmptyScheduleUI() {

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(R.drawable.ic_schedule), contentDescription = null, Modifier.size(222.dp))
        Spacer(modifier = Modifier.height(25.dp))
        CommonText(text = stringResource(R.string.no_schedules_are_set), fontWeight = FontWeight.Bold, size = 25.sp)
        Spacer(modifier = Modifier.height(10.dp))
        CommonText(text = stringResource(R.string.set_a_schedule_to_launch_your_app), modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium, size = 20.sp, color = Color.Gray)
    }
}


