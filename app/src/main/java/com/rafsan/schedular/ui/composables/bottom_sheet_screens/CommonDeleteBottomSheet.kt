package com.rafsan.schedular.ui.composables.bottom_sheet_screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rafsan.schedular.R
import com.rafsan.schedular.data.ScheduleEntity
import com.rafsan.schedular.ui.composables.components.CommonText
import com.rafsan.schedular.ui.theme.fonts
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonDeleteBottomSheet(state: Boolean, title: String,subTitle: String, schedule: ScheduleEntity?, onDelete: (ScheduleEntity) -> Unit, onDismiss:() -> Unit, actionButtonText: String) {
    if (state) {
        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val scope = rememberCoroutineScope()

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

                CommonText(title, size = 25.sp)
                Spacer(modifier = Modifier.height(20.dp))
                CommonText(text = subTitle, size = 16.sp, color = MaterialTheme.colorScheme.secondary.copy(alpha = .5f))
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    OutlinedButton(modifier = Modifier.weight(1f), onClick = {
                        scope.launch { bottomSheetState.hide() }
                        onDismiss()
                    }) {
                        Text(stringResource(R.string.cancel), fontFamily = fonts)
                    }

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            schedule?.let { onDelete(it) }
                            onDismiss()
                            scope.launch { bottomSheetState.hide() }
                        },
                    ) {
                        Text(actionButtonText, style = TextStyle(
                            fontFamily = fonts
                        ))
                    }
                }
            }
        }
    }

}

