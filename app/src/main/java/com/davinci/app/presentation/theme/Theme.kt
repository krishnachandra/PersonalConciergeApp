package com.davinci.app.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Davinci always uses a light theme — the "Airy Minimal Teal" design
 * is built on a pure white canvas with teal accents.
 */
private val DavinciColorScheme = lightColorScheme(
    primary = DavinciColors.Primary,
    onPrimary = DavinciColors.TextOnPrimary,
    primaryContainer = DavinciColors.PrimaryLight,
    onPrimaryContainer = DavinciColors.Primary,

    secondary = DavinciColors.TextSecondary,
    onSecondary = DavinciColors.TextOnPrimary,

    background = DavinciColors.Background,
    onBackground = DavinciColors.TextPrimary,

    surface = DavinciColors.Background,
    onSurface = DavinciColors.TextPrimary,
    surfaceVariant = DavinciColors.Surface,
    onSurfaceVariant = DavinciColors.TextMuted,

    error = DavinciColors.Error,
    onError = DavinciColors.TextOnPrimary,

    outline = DavinciColors.Divider,
    outlineVariant = DavinciColors.SurfaceVariant,
)

@Composable
fun DavinciTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DavinciColors.Background.toArgb()
            window.navigationBarColor = DavinciColors.Background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = DavinciColorScheme,
        typography = DavinciTypography,
        content = content,
    )
}
