package com.tpkprojects.academictracker

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tpkprojects.academictracker.ui.appviews.FlagView
import com.tpkprojects.academictracker.ui.appviews.HomeView
import com.tpkprojects.academictracker.ui.appviews.StartView
import com.tpkprojects.academictracker.ui.appviews.SubjectView
import com.tpkprojects.academictracker.ui.appviews.TestView
import com.tpkprojects.academictracker.ui.appviews.UserLoginView
import kotlinx.coroutines.launch

//this empties the navBackStack Completely
fun NavOptionsBuilder.popUpToTop(navController: NavHostController){
    popUpTo(navController.currentBackStackEntry?.destination?.route ?: return) {
        inclusive = true
    }
}
@Composable
fun Navigation(navController: NavHostController, viewModel: MainViewModel){

    NavHost(navController = navController, startDestination = Screen.BottomScreen.Summary.route){

//        composable(Screen.StartScreen.route){
//            StartView()
//        }
//
//        composable(Screen.UserLoginScreen.route){
//            UserLoginView(viewModel)
//        }

        composable(Screen.BottomScreen.Summary.route){
            if(viewModel.currentSubject.value== "Home") HomeView(viewModel)
            else SubjectView(viewModel);
        }

        composable(Screen.BottomScreen.Tests.route){
            TestView(viewModel);
        }
    }

    LaunchedEffect(navController) {
        val backStackEntryFlow = navController.currentBackStackEntryFlow
        backStackEntryFlow.collect { entry ->
            viewModel.setCurrentScreen(entry.destination?.route?: Screen.BottomScreen.Summary.route)
        }
    }
}