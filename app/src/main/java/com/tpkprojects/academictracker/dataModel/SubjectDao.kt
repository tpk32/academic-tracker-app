package com.tpkprojects.academictracker.dataModel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {
    @Insert
    suspend fun insertSubject(subject: Subject):Long

    @Delete//"DELETE FROM Subject WHERE uniqueSubjectId = :uniqueSubjectId")
    suspend fun deleteSubject(subject: Subject)

    @Query("SELECT * FROM Subject WHERE userId = :userId")
     fun getSubjectsByUserId(userId: String): Flow<List<Subject>>
}