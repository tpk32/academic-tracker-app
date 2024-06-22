package com.tpkprojects.academictracker.dataModel

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class AcademicRepository(private val userDao: UserDao, private val subjectDao: SubjectDao, private val testDao: TestDao, private val academicDao: AcademicDao){

    // User fun
    suspend fun addUser(user: User){
        userDao.insertUser(user)
    }

    suspend fun getUser(): User?{
        return userDao.getUser()
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    // Subject fun
    suspend fun addSubject(subject: Subject): Long{
        try {
            return subjectDao.insertSubject(subject)
        } catch (e: SQLiteConstraintException) {
            // Handle conflict (e.g., logging error)
            return -1 // Or any value indicating conflict
        }
    }

    suspend fun deleteSubject(subject: Subject){
        subjectDao.deleteSubject(subject)
    }

    fun getSubjectsByUserId(uniqueSubjectId: String) : Flow<List<Subject>> {
        return subjectDao.getSubjectsByUserId(uniqueSubjectId)
    }

    // Test fun
    suspend fun addTest(test: Test) : Long{
        try{
            return testDao.insertTest(test)
        } catch (e: SQLiteConstraintException) {
            // Handle conflict (e.g., logging error)
            return -1 // Or any value indicating conflict
        }
    }

    suspend fun deleteTestById(uniqueSubjectId: String, testId: Long){
        testDao.deleteTestById(uniqueSubjectId, testId)
    }

    fun getTestsById(uniqueSubjectId: String) : Flow<List<Test>> {
        return testDao.getTestsBySubjectId(uniqueSubjectId)
    }

    fun getAllTests(userId: String) : Flow<List<Test>>{
        return testDao.getAllTests(userId)
    }

    //HomeScreen fun
    fun getAveragePercentage(userId: String): Flow<Double?>{
        return academicDao.getAveragePercentage(userId)
    }

    fun getTestCount(userId: String): Flow<Int?>{
        return academicDao.getTestCount(userId)
    }

    fun getLatestTestDate(userId: String): Flow<LocalDate?>{
        return academicDao.getLatestTestDate(userId)
    }

    fun getSubjectWithMostTests(userId: String): Flow<String?>{
        return academicDao.getSubjectWithMostTests(userId)
    }

    // SubjectScreen fun
    fun getSubjectPercentage(userId: String, subjectName: String): Flow<Double?>{
        return academicDao.getSubjectPercentage(userId, subjectName)
    }

    fun getTestCountForSubject(userId: String, subjectName: String): Flow<Int?>{
        return academicDao.getTestCountForSubject(userId, subjectName)

    }

    fun getSubjectLastTestData(userId: String, subjectName: String): Flow<ShortTestData?>{
        return academicDao.getLastTestData(userId, subjectName)
    }

    fun getSubjectFirstTestData(userId: String, subjectName: String): Flow<ShortTestData?>{
        return academicDao.getFirstTestData(userId, subjectName)
    }
}