package com.example.margdarsh.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = RoyalBlue,
    onPrimary = CardBackground,
    primaryContainer = PaperSurface,
    onPrimaryContainer = MargNavy,
    secondary = AmberGold,
    onSecondary = CardBackground,
    secondaryContainer = PaperSurfaceWarm,
    onSecondaryContainer = AmberGoldDark,
    tertiary = MargNavy,
    onTertiary = CardBackground,
    background = PaperBackground,
    onBackground = InkTextPrimary,
    surface = CardBackground,
    onSurface = InkTextPrimary,
    surfaceVariant = PaperSurface,
    onSurfaceVariant = InkTextSecondary,
    outline = DividerColor,
    outlineVariant = InputBorder
)

@Composable
fun MargdarshTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
