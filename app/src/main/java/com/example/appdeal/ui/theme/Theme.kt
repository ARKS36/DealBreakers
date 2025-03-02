package com.example.appdeal.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

object ThemeState {
    var isDarkTheme by mutableStateOf(false)
}

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = OnPrimary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryDark,
    onSecondaryContainer = OnPrimary,
    tertiary = Accent,
    onTertiary = OnPrimary,
    tertiaryContainer = AccentDark,
    onTertiaryContainer = OnPrimary,
    error = Error,
    onError = OnPrimary,
    background = Color(0xFF1A1C1E),
    onBackground = OnPrimary,
    surface = Color(0xFF1A1C1E),
    onSurface = OnPrimary,
    surfaceVariant = Color(0xFF44474A),
    onSurfaceVariant = Color(0xFFE1E2E5)
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryLight,
    onPrimaryContainer = Color(0xFF00325B),
    secondary = Secondary,
    onSecondary = OnPrimary,
    secondaryContainer = SecondaryLight,
    onSecondaryContainer = Color(0xFF00344D),
    tertiary = Accent,
    onTertiary = OnPrimary,
    tertiaryContainer = Color(0xFFDDE5FF),
    onTertiaryContainer = Color(0xFF001849),
    error = Error,
    onError = OnPrimary,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant
)

@Composable
fun AppDealTheme(
    darkTheme: Boolean = ThemeState.isDarkTheme,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}