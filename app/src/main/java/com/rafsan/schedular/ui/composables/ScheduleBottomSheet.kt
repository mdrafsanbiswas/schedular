package com.rafsan.schedular.ui.composables


import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rafsan.schedular.R
import com.rafsan.schedular.data.ScheduleEntity
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleBottomSheet(
    state: Boolean,
    schedule: ScheduleEntity?,
    onSetSchedule: (Long) -> Unit,
    onCancelSchedule: () -> Unit,
    onDismiss: () -> Unit
) {
    if (state) {
        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()

        var selectedDateTime by remember { mutableStateOf<Long?>(null) }
        var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
        var selectedHour by remember { mutableStateOf(0) }
        var selectedMinute by remember { mutableStateOf(0) }

        var showDatePicker by remember { mutableStateOf(false) }
        var showTimePicker by remember { mutableStateOf(false) }

        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = bottomSheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = schedule?.appName ?: "", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                if (schedule?.isScheduled == true) {
                    val formattedTime =
                        SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault()).format(
                            Date(schedule.scheduleTime ?: 0L)
                        )
                    Text("Scheduled at: $formattedTime")
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            scope.launch { bottomSheetState.hide() }
                            onDismiss()
                        }) {
                            Text(stringResource(R.string.okay))
                        }
                        OutlinedButton(onClick = {
                            onCancelSchedule()
                            onDismiss()
                        }) {
                            Text(stringResource(R.string.cancel_schedule))
                        }
                    }
                } else {
                    Button(onClick = { showDatePicker = true }) {
                        Text(stringResource(R.string.pick_date_time))
                    }

                    selectedDateTime?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Selected Time: ${
                                SimpleDateFormat(
                                    "dd/MM/yyyy hh:mm a",
                                    Locale.getDefault()
                                ).format(Date(it))
                            }"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        OutlinedButton(modifier = Modifier.weight(1f), onClick = {
                            scope.launch { bottomSheetState.hide() }
                            onDismiss()
                        }) {
                            Text(stringResource(R.string.cancel))
                        }
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                selectedDateTime?.let { onSetSchedule(it) }
                                onDismiss()
                                scope.launch { bottomSheetState.hide() }
                            },
                            enabled = selectedDateTime != null
                        ) {
                            Text(stringResource(R.string.set_schedule))
                        }
                    }
                }
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = { showDatePicker = false; showTimePicker = true }) {
                        Text(stringResource(R.string.next))
                    }
                }
            ) {
                val datePickerState = rememberDatePickerState()
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
                onDismissRequest = { showTimePicker = false },
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
}




