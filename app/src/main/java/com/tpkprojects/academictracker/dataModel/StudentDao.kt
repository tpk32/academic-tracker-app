package com.tpkprojects.academictracker.dataModel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(student: Student)

    @Query("SELECT * FROM Student")
    suspend fun getStudent(): Student?

    @Delete
    suspend fun deleteStudent(student: Student)

}