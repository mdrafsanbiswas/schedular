package com.rafsan.schedular.ui.theme

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    secondary = colorTextDark,
    tertiary = colorTextLight, // button text color
    surface = colorSurfaceDark,
    background = colorBackGroundDark
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    secondary = colorTextLight,
    tertiary = colorTextDark, // button text color
    surface = colorSurfaceLight,
    background = colorBackGroundLight

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun SchedulerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    Log.d("check_theme", "$darkTheme")

    val view = LocalView.current
    val context = LocalContext.current


    val colors =  if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    if (!view.isInEditMode) {
        val window = (context as? Activity)?.window
        window?.statusBarColor = colors.primary.toArgb()
        val decorView = window?.decorView
        val isLightTheme = !darkTheme
        if (decorView != null) {
            WindowCompat.getInsetsController(window, decorView).isAppearanceLightStatusBars =
                isLightTheme
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}