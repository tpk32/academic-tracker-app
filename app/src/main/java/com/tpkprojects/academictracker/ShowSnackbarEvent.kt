package com.tpkprojects.academictracker

data class ShowSnackbarEvent(val message: String, val actionLabel: String? = null)
data class ShowToastEvent(val message: String)