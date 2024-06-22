package com.tpkprojects.academictracker.ui.appviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.tpkprojects.academictracker.MainViewModel
import com.tpkprojects.academictracker.Screen

//this screen is there to make the startDestination for navStackPopUp()

@Composable
fun FlagView(navController: NavHostController, viewModel: MainViewModel){
    if(viewModel.currentScreen.value == "" || viewModel.currentScreen.value==Screen.UserLoginScreen.route) {
        navController.navigate(Screen.BottomScreen.Summary.route)
        viewModel.setCurrentScreen(Screen.BottomScreen.Summary.route)
    }
}