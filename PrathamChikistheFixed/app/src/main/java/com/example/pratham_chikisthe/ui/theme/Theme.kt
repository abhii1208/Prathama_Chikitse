package com.example.pratham_chikisthe.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = EmergencyRed,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = EmergencyRedLight,
    onPrimaryContainer = EmergencyRedDark,
    secondary = MedicalBlue,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = MedicalBlueLight,
    background = SoftBackground,
    surface = SoftBackground
)

private val DarkColors = darkColorScheme(
    primary = ActionBlue,
    onPrimary = NightSurfaceAlt,
    primaryContainer = ActionBlueDark,
    onPrimaryContainer = NightTextPrimary,
    secondary = ActionBlueDark,
    onSecondary = NightTextPrimary,
    secondaryContainer = NightSurfaceAlt,
    onSecondaryContainer = NightTextPrimary,
    background = NightBackground,
    onBackground = NightTextPrimary,
    surface = NightSurface,
    onSurface = NightTextPrimary,
    surfaceVariant = NightSurfaceAlt,
    onSurfaceVariant = NightTextMuted
)

@Composable
fun PrathamChikistheTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
