package com.tpkprojects.academictracker.dataModel

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Subject",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Subject(
    @PrimaryKey val uniqueSubjectId: String,
    val userId: String,
    val subjectName: String
)
