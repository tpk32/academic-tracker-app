package com.tpkprojects.academictracker.dataModel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Query("""
        INSERT INTO Subject(studentId, subjectName)
        VALUES (:studentId, :subjectName)
    """)
    suspend fun insertSubject(studentId: String, subjectName: String):Long

    @Query("DELETE FROM Subject WHERE studentId = :studentId AND subjectName = :subjectName")
    suspend fun deleteSubject(studentId: String, subjectName: String)

    @Query("SELECT * FROM Subject WHERE studentId = :studentId")
     fun getSubjectsByStudentId(studentId: String): Flow<List<Subject>>

     @Query("SELECT subjectName FROM Subject WHERE subjectId = :subjectId AND studentId = :studentId")
     suspend fun getSubjectNameBySubjectId(studentId:String, subjectId: Long): String?

    @Query("SELECT subjectId FROM Subject WHERE subjectName = :subjectName AND studentId = :studentId")
    suspend fun getSubjectIdBySubjectName(studentId:String, subjectName: String): Long?
}