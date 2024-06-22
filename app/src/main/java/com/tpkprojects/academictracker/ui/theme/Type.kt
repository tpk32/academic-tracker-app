package com.tpkprojects.academictracker.ui.theme

import androidx.compose.material.Text
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.tpkprojects.academictracker.R

val nunito = FontFamily(
    Font(R.font.nunitosans_regular, FontWeight.Normal),
    Font(R.font.nunitosans_medium, FontWeight.Medium),
    Font(R.font.nunitosans_semibold, FontWeight.SemiBold),
    Font(R.font.nunitosans_bold, FontWeight.Bold)
)
// Set of Material typography styles to start with
val MyTypography = Typography(

    headlineLarge = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        //lineHeight = 28.sp,
        //letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        //lineHeight = 28.sp,
        //letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        //lineHeight = 28.sp,
        //letterSpacing = 0.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        //lineHeight = 22.sp,
        //letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        //lineHeight = 20.sp,
        //letterSpacing = 0.sp
    ),
    bodySmall = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        //lineHeight = 28.sp,
        //letterSpacing = 0.sp
    ),

)

