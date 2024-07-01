package com.tpkprojects.academictracker.presentation.signIn

import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.tpkprojects.academictracker.apiService.ApiViewModel
import com.tpkprojects.academictracker.apiService.idToken

fun handleFirebaseToken(apiViewModel: ApiViewModel) {
    try {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            firebaseUser.getIdToken(true) // Force refresh
                .addOnCompleteListener {
                    tokenTask->
                    if(tokenTask.isSuccessful){
                        val firebaseIdToken = tokenTask.result?.token
                        //use
                        val gotIdToken = idToken(firebaseIdToken)
                        Log.d("idToken", firebaseIdToken!!)
                        apiViewModel.loginWithGoogle(gotIdToken)
                    }else{
                        Log.d("logintag", "token unsuccessful")
                    }
                }
        }else{
            Log.e("logintag", "sign in failure")
        }
    } catch (e: ApiException) {
        Log.e("logintag", "handleSignInResult:error", e)
    }
}