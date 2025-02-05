package com.rafsan.schedular.ui.composables.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.rafsan.schedular.ui.theme.fonts

@Composable
fun CommonText(
    text: String = "Sample Text",
    fontWeight: FontWeight = FontWeight.Medium,
    color: Color = MaterialTheme.colorScheme.secondary,
    size: TextUnit = 20.sp,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = 1

) {
    Text(
        text = text,
        fontFamily = fonts,
        fontWeight = fontWeight,
        color = color,
        fontSize = size,
        modifier = modifier,
        style = style,
        overflow = overflow,
        maxLines = maxLines,
    )
}