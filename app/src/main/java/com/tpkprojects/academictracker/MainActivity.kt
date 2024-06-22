package com.tpkprojects.academictracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tpkprojects.academictracker.dataModel.User
import com.tpkprojects.academictracker.ui.appviews.StartView
import com.tpkprojects.academictracker.ui.appviews.UserLoginView
import com.tpkprojects.academictracker.ui.theme.AcademicTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AcademicTrackerTheme {
                val viewModel: MainViewModel = viewModel()
                val firstView = viewModel.firstView
                // 0 -> StartView
                // 1 -> UserLoginView
                // 2 -> MainView

                LaunchedEffect(Unit){
                    val userDao = Graph.database.userDao()
                    Log.d("shuru", "database")
                    viewModel.setUser(userDao.getUser())

                    val hasUser = (viewModel.user.value !=null )

                    Log.d("shuru", "emitting")
                    if(hasUser){
                        viewModel.initialiseModel()
                        viewModel.setFirstView(2)
                    }else{
                        viewModel.setCurrentScreen(Screen.UserLoginScreen.route)
                        viewModel.setFirstView(1)
                    }
                }

                when(firstView.value){
                    0 -> StartView()
                    1 -> UserLoginView(viewModel = viewModel)
                    2 -> MainView(viewModel = viewModel)
                }
            }
        }
    }
}
