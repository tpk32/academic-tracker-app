package com.tpkprojects.academictracker.apiService

import com.tpkprojects.academictracker.dataModel.Subject
import com.tpkprojects.academictracker.dataModel.SubjectList
import com.tpkprojects.academictracker.dataModel.Test
import com.tpkprojects.academictracker.dataModel.TestList
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private val retrofit = Retrofit.Builder().baseUrl("https://academic-tracker-server-production.up.railway.app/api/v1/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val apiService = retrofit.create(ApiService::class.java)

interface ApiService {

    @POST("student/login")
    suspend fun loginWithGoogle(@Body idToken: idToken): LoginResponse

    @POST("student/subject")
    suspend fun createSubject(@Body subject: Subject): apiRespopnse

    @POST("student/subject/test/{student_id}/{subject_name}")
    suspend fun createTest(
        @Path("student_id") student_id: String,
        @Path("subject_name") subjectName: String,
        @Body test: Test
    ): apiRespopnse

    @DELETE("student/subject/{student_id}")
    suspend fun deleteAllSubjectsByStudentId(@Path("student_id") studentId: String): apiRespopnse

    @GET("student/subject/{student_id}")
    suspend fun getAllSubjectsByStudentId(@Path("student_id") studentId: String): SubjectList

    @GET("student/test/{student_id}/{subject_name}")
    suspend fun getAllTestsBySubjectName(
        @Path("student_id") studentId: String,
        @Path("subject_name") subjectName: String
    ): TestList
}