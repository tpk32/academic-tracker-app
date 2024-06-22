package com.tpkprojects.academictracker

import androidx.annotation.DrawableRes

sealed class Screen(val title: String, val route: String){

    sealed class BottomScreen(val bTitle: String, val bRoute: String, @DrawableRes val icon: Int, isSelected: Boolean):Screen(bTitle, bRoute){
        object Summary: BottomScreen("Summary", "summary", R.drawable.ic_summary, false)
        object Tests: BottomScreen("Tests", "tests", R.drawable.ic_tests, false)
    }

    object UserLoginScreen: Screen(title = "Login", route = "login")
    object StartScreen: Screen(title = "Start", route = "start")
}

val screensInBottom = listOf(
    Screen.BottomScreen.Summary,
    Screen.BottomScreen.Tests
)

