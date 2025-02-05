package com.rafsan.schedular.ui.composables.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

@Composable
fun CommonIcon(imageVector: ImageVector,size: Dp, onClick: ()-> Unit = {}) {
    Icon(imageVector = imageVector, contentDescription = null, modifier = Modifier.size(size).clickable {
        onClick()
    })
}