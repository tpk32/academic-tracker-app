package com.tpkprojects.academictracker.dataModel

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class AcademicRepository(private val studentDao: StudentDao, private val subjectDao: SubjectDao, private val testDao: TestDao, private val academicDao: AcademicDao){

    // User fun
    suspend fun addStudent(student: Student){
        studentDao.insertStudent(student)
    }

    suspend fun getStudent(): Student?{
        return studentDao.getStudent()
    }

    suspend fun deleteStudent(student: Student){
        studentDao.deleteStudent(student)
    }

    // Subject fun
    suspend fun addSubject(subject: Subject): Long{
        try {
            return subjectDao.insertSubject(subject.studentId, subject.subjectName)
        } catch (e: SQLiteConstraintException) {
            // Handle conflict (e.g., logging error)
            return -1 // Or any value indicating conflict
        }
    }

    suspend fun deleteSubject(studentId: String, subject: Subject){
        subjectDao.deleteSubject(studentId, subject.subjectName)
    }

    fun getSubjectsByStudentId(studentId: String) : Flow<List<Subject>> {
        return subjectDao.getSubjectsByStudentId(studentId)
    }

    suspend fun getSubjectNameBySubjectId(studentId: String, subjectId: Long) : String?{
        return subjectDao.getSubjectNameBySubjectId(studentId, subjectId)
    }

    suspend fun getSubjectIdBySubjectName(studentId: String, subjectName: String) : Long?{
        return subjectDao.getSubjectIdBySubjectName(studentId, subjectName)
    }

    // Test fun
    suspend fun addTest(test: Test) : Long{
        try{
            return testDao.insertTest(test)
        } catch (e: SQLiteConstraintException) {
            Log.e("test", "Error adding test: ${e.message}")
            // Handle conflict (e.g., logging error)
            return -1 // Or any value indicating conflict

        }
    }

    suspend fun addTestBySubjectName(test:Test, subjectName: String){
        try{
            testDao.addTestBySubjectName(test.testName, test.testDate, test.maxMarks, test.obtainedMarks, subjectName)
        } catch (e: SQLiteConstraintException) {
            Log.e("test", "Error adding test: ${e.message}")
            // Handle conflict (e.g., logging error)
        // Or any value indicating conflict
        }
    }

    suspend fun deleteTest(studentId: String, subject: String, test:Test){
        testDao.deleteTest(studentId, subject, test.testName, test.testDate)
    }

    fun getTestsBySubjectName(studentId:String, subject: String) : Flow<List<Test>> {
        return testDao.getTestsBySubjectName(studentId, subject)
    }

    fun getAllTests(studentId: String) : Flow<List<TestWithSubject>>{
        return testDao.getAllTestsWithSubject(studentId)
    }

    //HomeScreen fun
    fun getAveragePercentage(studentId: String): Flow<Double?>{
        return academicDao.getAveragePercentage(studentId)
    }

    fun getTestCount(studentId: String): Flow<Int?>{
        return academicDao.getTestCount(studentId)
    }

    fun getLatestTestDate(studentId: String): Flow<LocalDate?>{
        return academicDao.getLatestTestDate(studentId)
    }

    fun getSubjectWithMostTests(studentId: String): Flow<String?>{
        return academicDao.getSubjectWithMostTests(studentId)
    }

    // SubjectScreen fun
    fun getSubjectPercentage(studentId: String, subjectName: String): Flow<Double?>{
        return academicDao.getSubjectPercentage(studentId, subjectName)
    }

    fun getTestCountForSubject(studentId: String, subjectName: String): Flow<Int?>{
        return academicDao.getTestCountForSubject(studentId, subjectName)

    }

    fun getSubjectLastTestData(studentId: String, subjectName: String): Flow<ShortTestData?>{
        return academicDao.getLastTestData(studentId, subjectName)
    }

    fun getSubjectFirstTestData(studentId: String, subjectName: String): Flow<ShortTestData?>{
        return academicDao.getFirstTestData(studentId, subjectName)
    }
}