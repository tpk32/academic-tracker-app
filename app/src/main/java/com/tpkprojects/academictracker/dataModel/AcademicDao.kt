package com.tpkprojects.academictracker.dataModel

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface AcademicDao{

    // Get the average percentage over all the tests
    @Transaction
    @Query(
        """
        SELECT AVG(CAST(t.ObtainedMarks AS FLOAT) / t.MaxMarks * 100) AS averagePercentage
        FROM Test t
        INNER JOIN Subject s ON t.subjectId = s.subjectId
        INNER JOIN Student stu ON s.studentId = stu.studentId
        WHERE stu.studentId = :studentId
    """
    )
    fun getAveragePercentage(studentId: String): Flow<Double?>

    // Get the number of tests the user has taken in total
    @Transaction
    @Query(
        """
        SELECT COUNT(*) AS testCount
        FROM Test t
        INNER JOIN Subject s ON t.subjectId = s.subjectId
        INNER JOIN Student stu ON s.studentId = stu.studentId
        WHERE stu.studentId = :studentId
    """
    )
    fun getTestCount(studentId: String): Flow<Int?>

    // Get the date of the last Test user took
    @Transaction
    @Query(
        """
        SELECT MAX(testDate) AS latestTestDate
        FROM Test t
        INNER JOIN Subject s ON t.subjectId = s.subjectId
        INNER JOIN Student stu ON s.studentId = stu.studentId
        WHERE stu.studentId = :studentId
    """
    )
    fun getLatestTestDate(studentId: String): Flow<LocalDate?>

    // Get the subject which had the most number of test
    @Transaction
    @Query(
        """
        SELECT s.subjectName AS subjectWithMostTests
        FROM Subject s
        INNER JOIN Test t ON t.subjectId = s.subjectId
        INNER JOIN Student stu ON s.studentId = stu.studentId
        WHERE stu.studentId = :studentId
        GROUP BY s.subjectName
        ORDER BY COUNT(t.testID) DESC
        LIMIT 1
    """
    )
    fun getSubjectWithMostTests(studentId: String): Flow<String?>

    // Get the average percentage of a particular subject
    @Transaction
    @Query(
        """
    SELECT AVG(CAST(t.ObtainedMarks AS FLOAT) / t.MaxMarks * 100) AS subjectPercentage
    FROM Test t
    INNER JOIN Subject s ON t.subjectId = s.subjectId
    INNER JOIN Student stu ON s.studentId = stu.studentId
    WHERE stu.studentId = :studentId AND s.subjectName = :subjectName
    """
    )
    fun getSubjectPercentage(studentId: String, subjectName: String): Flow<Double?>

    // Get the total number of tests for a particular subject
    @Transaction
    @Query(
        """
        SELECT COUNT(*) AS testCountForSubject
        FROM Test t
        INNER JOIN Subject s ON t.subjectId = s.subjectId
        INNER JOIN Student stu ON s.studentId = stu.studentId
    WHERE stu.studentId = :studentId AND s.subjectName = :subjectName
    """
    )
    fun getTestCountForSubject(studentId: String, subjectName: String): Flow<Int?>

    // Get the last test data for a particular subject
    @Transaction // Required for joins across multiple tables
    @Query(
        """
        SELECT t.testDate AS thisTestDate, t.ObtainedMarks, t.MaxMarks
        FROM Test t
        INNER JOIN Subject s ON t.subjectId = s.subjectId
        INNER JOIN Student stu ON s.studentId = stu.studentId
        WHERE stu.studentId = :studentId AND s.subjectName = :subjectName
        ORDER BY t.testDate DESC
        LIMIT 1
    """
    )
    fun getLastTestData(studentId: String, subjectName: String): Flow<ShortTestData?>

    // Get the first test data for a particular subject
    @Transaction // Required for joins across multiple tables
    @Query(
        """
        SELECT t.testDate AS thisTestDate, t.ObtainedMarks, t.MaxMarks
        FROM Test t
        INNER JOIN Subject s ON t.subjectId = s.subjectId
        INNER JOIN Student stu ON s.studentId = stu.studentId
        WHERE stu.studentId = :studentId AND s.subjectName = :subjectName
        ORDER BY t.testDate DESC
        LIMIT 1
    """
    )
    fun getFirstTestData(studentId: String, subjectName: String): Flow<ShortTestData?>
}