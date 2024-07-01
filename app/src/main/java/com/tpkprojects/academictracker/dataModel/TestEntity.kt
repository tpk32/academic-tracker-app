package com.tpkprojects.academictracker.dataModel

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
/*
in backend primary keys are uniqueSubjectId and testID , that is it is a composite key
here to make testID autogenerate, we used testID as Primary key as it can be primary in the offline database
 */

@Entity(
    tableName = "Test",
    indices = [Index(value = ["subjectId", "testName", "testDate"], unique = true)],
    //primaryKeys = ["uniqueSubjectId", "testID"],
    foreignKeys = [ForeignKey(
        entity = Subject::class,
        parentColumns = ["subjectId"],
        childColumns = ["subjectId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Test(
    @PrimaryKey(autoGenerate = true)
    val testId: Long = 0L,
    @SerializedName("test_id") val subjectId: Long,
    @SerializedName("test_name")val testName: String,
    @SerializedName("test_date") @JsonAdapter(LocalDateAsStringAdapter::class)val testDate: LocalDate,
    @SerializedName("max_marks")val maxMarks: Int,
    @SerializedName("obtained_marks")val obtainedMarks: Int,
   // @Expose val syncStatus: SyncStatus = SyncStatus.PENDING
)

data class TestList(
    @SerializedName("tests")val tests: List<Test>
)
data class TestWithSubject(
    @Embedded val test: Test,
    val subjectName: String
)
