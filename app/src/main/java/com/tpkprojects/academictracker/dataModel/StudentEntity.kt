package com.tpkprojects.academictracker.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Student")
data class Student(
    @PrimaryKey @SerializedName("student_id") val studentId: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String
)