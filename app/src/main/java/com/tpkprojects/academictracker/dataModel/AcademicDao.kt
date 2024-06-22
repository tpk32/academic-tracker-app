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
    @Query("""
        SELECT AVG(CAST(t.ObtainedMarks AS FLOAT) / t.MaxMarks * 100) AS averagePercentage
        FROM Test t
        INNER JOIN Subject s ON t.uniqueSubjectId = s.uniqueSubjectId
        INNER JOIN User u ON s.userId = u.userId
        WHERE u.userId = :userId
    """)
    fun getAveragePercentage(userId: String): Flow<Double?>

    // Get the number of tests the user has taken in total
    @Transaction
    @Query("""
        SELECT COUNT(*) AS testCount
        FROM Test t
        INNER JOIN Subject s ON t.uniqueSubjectId = s.uniqueSubjectId
        INNER JOIN User u ON s.userId = u.userId
        WHERE u.userId = :userId
    """)
    fun getTestCount(userId: String): Flow<Int?>

    // Get the date of the last Test user took
    @Transaction
    @Query("""
        SELECT MAX(testDate) AS latestTestDate
        FROM Test t
        INNER JOIN Subject s ON t.uniqueSubjectId = s.uniqueSubjectId
        INNER JOIN User u ON s.userId = u.userId
        WHERE u.userId = :userId
    """)
    fun getLatestTestDate(userId: String): Flow<LocalDate?>

    // Get the subject which had the most number of test
    @Transaction
    @Query("""
        SELECT s.subjectName AS subjectWithMostTests
        FROM Subject s
        INNER JOIN Test t ON t.uniqueSubjectId = s.uniqueSubjectId
        INNER JOIN User u ON s.userId = u.userId
        WHERE u.userId = :userId
        GROUP BY s.subjectName
        ORDER BY COUNT(t.testID) DESC
        LIMIT 1
    """)
    fun getSubjectWithMostTests(userId: String): Flow<String?>

    // Get the average percentage of a particular subject
    @Transaction
    @Query("""
    SELECT AVG(CAST(t.ObtainedMarks AS FLOAT) / t.MaxMarks * 100) AS subjectPercentage
    FROM Test t
    INNER JOIN Subject s ON t.uniqueSubjectId = s.uniqueSubjectId
    INNER JOIN User u ON s.userId = u.userId
    WHERE u.userId = :userId AND s.subjectName = :subjectName
    """)
    fun getSubjectPercentage(userId: String, subjectName: String): Flow<Double?>

    // Get the total number of tests for a particular subject
    @Transaction
    @Query("""
        SELECT COUNT(*) AS testCountForSubject
        FROM Test t
        INNER JOIN Subject s ON t.uniqueSubjectId = s.uniqueSubjectId
        INNER JOIN User u ON s.userId = u.userId
        WHERE u.userId = :userId AND s.subjectName = :subjectName
    """)
    fun getTestCountForSubject(userId: String, subjectName: String): Flow<Int?>

    // Get the last test data for a particular subject
    @Transaction // Required for joins across multiple tables
    @Query("""
        SELECT t.testDate AS thisTestDate, t.ObtainedMarks, t.MaxMarks
        FROM Test t
        INNER JOIN Subject s ON t.uniqueSubjectId = s.uniqueSubjectId
        INNER JOIN User u ON s.userId = u.userId
        WHERE u.userId = :userId AND s.subjectName = :subjectName
        ORDER BY t.testDate DESC
        LIMIT 1
    """)
    fun getLastTestData(userId: String, subjectName: String): Flow<ShortTestData?>

    // Get the first test data for a particular subject
    @Transaction // Required for joins across multiple tables
    @Query("""
        SELECT t.testDate AS thisTestDate, t.ObtainedMarks, t.MaxMarks
        FROM Test t
        INNER JOIN Subject s ON t.uniqueSubjectId = s.uniqueSubjectId
        INNER JOIN User u ON s.userId = u.userId
        WHERE u.userId = :userId AND s.subjectName = :subjectName
        ORDER BY t.testDate ASC
        LIMIT 1
    """)
    fun getFirstTestData(userId: String, subjectName: String): Flow<ShortTestData?>
}