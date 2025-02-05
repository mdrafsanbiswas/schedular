package com.rafsan.schedular.ui.composables.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

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
