package com.tpkprojects.academictracker.dataModel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TestDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTest(test: Test): Long

    @Query("""
        INSERT INTO Test (subjectId, testName, testDate, maxMarks, obtainedMarks)
        SELECT subjectId, :testName, :testDate, :maxMarks, :obtainedMarks
        FROM Subject
        WHERE subjectName = :subjectName;
    """)
    suspend fun addTestBySubjectName(testName: String, testDate: LocalDate, maxMarks: Int, obtainedMarks: Int, subjectName: String)

    @Transaction
    @Query(
        """SELECT t.*, s.subjectName
            FROM Test t NATURAL JOIN Subject s
            WHERE s.studentId = :studentId
            ORDER BY t.testDate DESC
        """
    )
    fun getAllTestsWithSubject(studentId: String): Flow<List<TestWithSubject>>

    @Query(
        """SELECT * FROM Test t
            INNER JOIN Subject s ON t.subjectId = s.subjectId
            INNER JOIN Student stu ON stu.studentId = s.studentId
            WHERE stu.studentId = :studentId AND s.subjectName = :subjectName
            ORDER BY testDate DESC
        """
    )
    fun getTestsBySubjectName(studentId: String, subjectName: String): Flow<List<Test>>

    @Query("""
        DELETE FROM Test
        WHERE subjectId =(
            SELECT subjectId
            FROM Subject
            WHERE studentId = :studentId AND subjectName = :subjectName
        ) AND testName = :testName AND testDate = :testDate
        """
    )
    suspend fun deleteTest(studentId: String, subjectName: String, testName: String, testDate: LocalDate)
}