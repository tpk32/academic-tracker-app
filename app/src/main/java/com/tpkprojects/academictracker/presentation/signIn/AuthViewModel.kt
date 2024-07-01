package com.tpkprojects.academictracker.presentation.signIn

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tpkprojects.academictracker.ShowToastEvent
import com.tpkprojects.academictracker.apiService.ApiViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class AuthViewModel: ViewModel(){
    var _progressBarVisible = mutableStateOf(false)
    val progressBarVisible: MutableState<Boolean> get() = _progressBarVisible
    fun setProgressBarVisible(visible: Boolean) {
        _progressBarVisible.value = visible
    }
    val loadingState = MutableStateFlow(LoadingState.IDLE)
    val loggedIn = MutableStateFlow(false)

    fun setLoggedInState(b: Boolean){
        loggedIn.value = b
    }

    fun signWithCredential(credential: AuthCredential) = viewModelScope.launch {
        try {
            withTimeout(5000) {
                loadingState.emit(LoadingState.LOADING)
                Log.d("logintag", "fbauth start")
                Firebase.auth.signInWithCredential(credential).await()
                Log.d("logintag", "done")
                if (loggedIn.value == false) loggedIn.value = true
                loadingState.emit(LoadingState.LOADED)
            }
        } catch (e: Exception) {
            loadingState.emit(LoadingState.error(e.localizedMessage))
            Log.e("logintag", "timeout")
            setProgressBarVisible(false)
            triggerToast("Cannot reach server, check internet connectivity")
        }
    }

    // toast events
    var _toastEvent = MutableStateFlow<ShowToastEvent?>(null)
    val toastEvent = _toastEvent.asStateFlow()
    fun setToastEvent(event : ShowToastEvent?) {
        _toastEvent.value = event
    }
    fun triggerToast(message: String) {
        _toastEvent.value = ShowToastEvent(message)
    }
}