package com.tpkprojects.academictracker.ui.theme

import androidx.compose.ui.graphics.Color

sealed class ThemeColors(val primary: Color,val onPrimary: Color,val secondary: Color,val onSecondary: Color, val tertiary:Color, val onTertiary:Color,  val surface: Color, val onSurface: Color, val surfaceSelected: Color, val onSurfaceSelected: Color,val background: Color, val onBackground: Color){

    object Dark: ThemeColors(
        primary = Color(0xFF59CCC5), // Your desired dark theme primary color
        onPrimary = Color.Black,
        secondary= Color(0xFF9171E4),
        onSecondary = Color.Black,
        tertiary = Color(0xFFE6E6E6),
        onTertiary = Color.Black,
        surface = Color(0xFF292931),
        onSurface = Color(0xFFE6E6E6),
        surfaceSelected = Color(0xFF404048),
        onSurfaceSelected = Color(0xFF909499),
        background = Color(0xFF060606),
        onBackground = Color(0xFF707075)
    )
    object Light: ThemeColors(
        primary = Color(0xFF9901DA), // Your desired dark theme primary color
        onPrimary = Color.White,
        secondary= Color(0xFF3847CC),
        onSecondary = Color.White,
        tertiary = Color(0xFF000000),
        onTertiary = Color(0xFFFDFAFA),
        surface = Color(0xFFE8E8EE),
        onSurface = Color.Black,
        surfaceSelected = Color(0xFFD0D0E4),
        onSurfaceSelected = Color(0xFF95959E),
        background = Color.White,
        onBackground = Color(0xFFAFAFBD)
    )
}
