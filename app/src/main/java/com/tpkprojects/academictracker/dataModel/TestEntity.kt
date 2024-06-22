package com.tpkprojects.academictracker.dataModel

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import java.time.LocalDate
/*
in backend primary keys are uniqueSubjectId and testID , that is it is a composite key
here to make testID autogenerate, we used testID as Primary key as it can be primary in the offline database
 */

@Entity(
    tableName = "Test",
    indices = [Index(value = ["uniqueSubjectId", "testName", "testDate"], unique = true)],
    //primaryKeys = ["uniqueSubjectId", "testID"],
    foreignKeys = [ForeignKey(
        entity = Subject::class,
        parentColumns = ["uniqueSubjectId"],
        childColumns = ["uniqueSubjectId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Test(
    @PrimaryKey(autoGenerate = true)
    val testID: Long = 0L,
    val uniqueSubjectId: String,
    val subjectName: String,
    val testName: String,
    val testDate: LocalDate,
    val maxMarks: Int,
    val obtainedMarks: Int,
)
