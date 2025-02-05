package com.rafsan.schedular.ui.composables.bottom_sheet_screens

import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rafsan.schedular.R
import com.rafsan.schedular.data.ScheduleEntity
import com.rafsan.schedular.ui.composables.components.CommonIcon
import com.rafsan.schedular.ui.composables.components.CommonText
import com.rafsan.schedular.ui.composables.InstalledApps
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListBottomSheet(
    state: Boolean,
    apps: List<ScheduleEntity>,
    onDismiss: () -> Unit,
    onAppClick: (ScheduleEntity) -> Unit,
    appIconMap: Map<String, Drawable>?
) {
    if (state) {
        val bottomSheetState =
            rememberModalBottomSheetState(skipPartiallyExpanded = false, confirmValueChange = { newState ->
                newState != SheetValue.Hidden
            })
        val scope = rememberCoroutineScope()

        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val maxSheetHeight = screenHeight * 0.6f

        ModalBottomSheet(
            onDismissRequest = {  },
            sheetState = bottomSheetState
        ) {
            Column(modifier = Modifier
                .padding(start = 25.dp, end = 25.dp)
                .heightIn(max = maxSheetHeight)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    CommonText(stringResource(R.string.select_app))

                    CommonIcon(imageVector = Icons.Filled.Close, size = 20.dp, onClick = {
                        scope.launch {
                            bottomSheetState.hide()
                        }
                        onDismiss()
                    })
                }
                Spacer(modifier = Modifier.height(30.dp))

                InstalledApps(apps,appIconMap = appIconMap, onAppClick = onAppClick)
            }

        }

    }
}