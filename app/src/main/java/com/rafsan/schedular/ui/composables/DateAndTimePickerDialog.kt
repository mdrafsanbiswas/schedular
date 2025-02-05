package com.rafsan.schedular.ui.composables

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rafsan.schedular.R
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateAndTimePickerDialog(state: Boolean, onDismiss: () -> Unit, onSetSchedule: (Long) -> Unit) {
    val currentTimeMillis = System.currentTimeMillis()

    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)

    var selectedDateTime by remember { mutableStateOf<Long?>(null) }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }

    var selectedHour by remember { mutableStateOf(currentHour) }
    var selectedMinute by remember { mutableStateOf(currentMinute) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
         showDatePicker = state
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = { showDatePicker = false; showTimePicker = true }) {
                    Text(stringResource(R.string.next))
                }
            }
        ) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = currentTimeMillis)
            DatePicker(state = datePickerState)
            LaunchedEffect(datePickerState.selectedDateMillis) {
                datePickerState.selectedDateMillis?.let { selectedMillis ->
                    selectedDateMillis = selectedMillis
                }
            }
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = {  },
            confirmButton = {
                TextButton(onClick = {
                    showTimePicker = false
                    selectedDateMillis?.let { dateMillis ->
                        val calendar = Calendar.getInstance().apply {
                            timeInMillis = dateMillis
                            set(Calendar.HOUR_OF_DAY, selectedHour)
                            set(Calendar.MINUTE, selectedMinute)
                            set(Calendar.SECOND, 0)
                        }
                        selectedDateTime = calendar.timeInMillis
                    }
                    selectedDateTime?.let { onSetSchedule(it) }
                    onDismiss()
                }) {
                    Text(stringResource(R.string.ok))
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.selected_time), style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    val timePickerState = rememberTimePickerState(
                        initialHour = selectedHour,
                        initialMinute = selectedMinute
                    )
                    TimePicker(state = timePickerState)

                    LaunchedEffect(timePickerState.hour, timePickerState.minute) {
                        selectedHour = timePickerState.hour
                        selectedMinute = timePickerState.minute
                    }
                }
            }
        )
    }
}