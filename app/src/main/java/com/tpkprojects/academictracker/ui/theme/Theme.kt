package com.tpkprojects.academictracker.ui.theme

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

//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//)

private val CustomDarkColorScheme = darkColorScheme(
    primary = ThemeColors.Dark.primary,
    onPrimary = ThemeColors.Dark.onPrimary,
    secondary = ThemeColors.Dark.secondary,
    onSecondary = ThemeColors.Dark.onSecondary,
    tertiary = ThemeColors.Dark.tertiary,
    onTertiary = ThemeColors.Dark.onTertiary,
    surface = ThemeColors.Dark.surface,
    onSurface = ThemeColors.Dark.onSurface,
    surfaceVariant = ThemeColors.Dark.surfaceSelected,
    onSurfaceVariant = ThemeColors.Dark.onSurfaceSelected,
    background = ThemeColors.Dark.background,
    onBackground = ThemeColors.Dark.onBackground
)

private val CustomLightColorScheme = darkColorScheme(
    primary = ThemeColors.Light.primary,
    onPrimary = ThemeColors.Light.onPrimary,
    secondary = ThemeColors.Light.secondary,
    onSecondary = ThemeColors.Light.onSecondary,
    tertiary = ThemeColors.Light.tertiary,
    onTertiary = ThemeColors.Light.onTertiary,
    surface = ThemeColors.Light.surface,
    onSurface = ThemeColors.Light.onSurface,
    surfaceVariant = ThemeColors.Light.surfaceSelected,
    onSurfaceVariant = ThemeColors.Light.onSurfaceSelected,
    background = ThemeColors.Light.background,
    onBackground = ThemeColors.Light.onBackground
)

@Composable
fun AcademicTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> CustomDarkColorScheme
        else -> CustomLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MyTypography,
        content = content
    )
}