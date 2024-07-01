package com.tpkprojects.academictracker.dataModel

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//enum class SyncStatus(val stat: String){ PENDING("pending"), SYNCED("synced") }

@Entity(
    tableName = "Subject",
    foreignKeys = [ForeignKey(
        entity = Student::class,
        parentColumns = ["studentId"],
        childColumns = ["studentId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Subject(
    @PrimaryKey(autoGenerate = true)val subjectId: Long = 0L,
    @SerializedName("student_id")val studentId: String,
    @SerializedName("subject_name")val subjectName: String,
    //@Expose val syncStatus : SyncStatus = SyncStatus.PENDING
)

data class SubjectList(
    @SerializedName("subjects")val subjects: List<Subject>
)
