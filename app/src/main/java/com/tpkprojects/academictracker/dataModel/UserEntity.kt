package com.tpkprojects.academictracker.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey val userId: String,
    val name: String,
    val email: String
)