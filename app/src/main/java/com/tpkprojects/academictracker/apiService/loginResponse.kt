package com.tpkprojects.academictracker.apiService

import com.google.gson.annotations.SerializedName
import com.tpkprojects.academictracker.dataModel.Student

data class idToken(
    val idtoken : String?
)

data class LoginResponse(
    @SerializedName("error")val Error: Boolean,
    @SerializedName("message")val Message: String,
    @SerializedName("data")val Data: Student
)

data class apiRespopnse(
    @SerializedName("error")val Error: Boolean,
    @SerializedName("message")val Message: String,
    @SerializedName("data")val Data: Any
)