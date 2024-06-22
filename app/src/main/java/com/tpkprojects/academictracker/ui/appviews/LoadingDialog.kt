package com.tpkprojects.academictracker.ui.appviews

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun ShowLoadingDialog(){
    Dialog(
        onDismissRequest = {},
    ) {
        CircularProgressIndicator()
    }
}