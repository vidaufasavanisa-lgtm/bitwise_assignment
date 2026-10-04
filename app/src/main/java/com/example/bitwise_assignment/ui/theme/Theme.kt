package com.example.bitwise_assignment.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AccentPeriwinkle,
    secondary = AccentPurple,
    tertiary = AccentPink,
    background = AccentDeepMidnight,
    surface = AccentDarkPurple,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = AccentDeepMidnight,
    onBackground = AccentPink,
    onSurface = AccentPink
)

private val LightColorScheme = lightColorScheme(
    primary = AccentPeriwinkle,
    secondary = AccentPurple,
    tertiary = AccentDarkPurple,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = AccentDeepMidnight,
    onSurface = AccentDeepMidnight
)

@Composable
fun Bitwise_assignmentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
