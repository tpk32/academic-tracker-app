package com.tpkprojects.academictracker.apiService

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.tpkprojects.academictracker.Graph
import com.tpkprojects.academictracker.R
import com.tpkprojects.academictracker.ShowToastEvent
import com.tpkprojects.academictracker.dataModel.AcademicRepository
import com.tpkprojects.academictracker.dataModel.Student
import com.tpkprojects.academictracker.dataModel.Subject
import com.tpkprojects.academictracker.dataModel.SubjectList
import com.tpkprojects.academictracker.dataModel.Test
import com.tpkprojects.academictracker.dataModel.TestList
import com.tpkprojects.academictracker.dataModel.TestWithSubject
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout


class ApiViewModel(
    private val academicRepository: AcademicRepository = Graph.academicRepository
) : ViewModel(){
    var _response = MutableStateFlow<LoginResponse?> (null)
    val response = _response

    fun resetResponse(){
        _response.value = null
    }

    fun loginWithGoogle(idToken: idToken){
        viewModelScope.launch {
            try{
                setProgressBarVisible(true)
                _response.value = apiService.loginWithGoogle(idToken)
                setProgressBarVisible(false)
                Log.d("logintag", "LoginSuccess:${_response.value?.Data}")
            }catch(e : Exception){
                Log.e("logintag", "LoginFailed retrofit : $e")
                setProgressBarVisible(false)
                triggerToast("Can't reach servers")
            }
        }
    }

    fun syncToBackend(student: Student, subjectList: List<Subject>, testWithSubjectList: List<TestWithSubject> ){
        setProgressBarVisible(true)

        viewModelScope.launch {
            delay(1500)
            try{
                Log.d("synctag", "Syncstart")
                viewModelScope.async { withTimeout(5000) { apiService.deleteAllSubjectsByStudentId(student.studentId) } }.await()
                viewModelScope.async { withTimeout(5000) { subjectList.forEach{ apiService.createSubject(it)} } }.await()
                viewModelScope.async { withTimeout(5000) { testWithSubjectList.forEach{ Log.d("synctag", "${it}\n");apiService.createTest(student.studentId, it.subjectName, it.test)} } }.await()
                Log.d("synctag", "333333333")
                triggerToast("Synced Successfully")
                setProgressBarVisible(false)
                Log.d("synctag", "Syncend")
            }catch (e: Exception){
                Log.e("synctag", "SyncFailed : $e")
                setProgressBarVisible(false)
                triggerToast("Sync Failed")
            }
        }
    }

    fun fetchData(studentId: String){
        viewModelScope.launch {
            try{
                var subjectList= SubjectList(subjects = listOf())
                var testList : TestList

                viewModelScope.async{ withTimeout(5000){ subjectList = apiService.getAllSubjectsByStudentId(studentId)} }.await()
                viewModelScope.async{ withTimeout(5000){ subjectList.subjects.forEach{ academicRepository.addSubject(it) } }; Log.d("test","subs added") }.await()
                viewModelScope.async{
                    withTimeout(5000){
                        subjectList.subjects.forEach{subject->
                            testList = apiService.getAllTestsBySubjectName(studentId, subject.subjectName)
                             testList.tests.forEach{
                                 academicRepository.addTestBySubjectName(it, subject.subjectName)
                                 Log.d("test","${it}")
                             }
                        }
                    }
                    setProgressBarVisible(false)
                }.await()
                Log.d("logintag", "fetched")

            }catch(e: Exception){
                Log.e("logintag", e.message.toString())
                setProgressBarVisible(false)
            }
        }
    }

    var allTestsWithSubject : Flow<List<TestWithSubject>> = emptyFlow()
    fun getAllTests(userId: String) {
        viewModelScope.launch {
            allTestsWithSubject = academicRepository.getAllTests(userId)
        }
    }

    var getSubjectsByStudentId: Flow<List<Subject>> = flowOf(listOf())
    fun getSubjectsByStudentId(student: Student){
        viewModelScope.launch {
            Log.d("usermsg", "subject List")
            getSubjectsByStudentId = academicRepository.getSubjectsByStudentId(student.studentId)
            Log.d("usermsg", "list end")
        }
    }


    // toast event
    var _toastEvent = MutableStateFlow<ShowToastEvent?>(null)
    val toastEvent = _toastEvent.asStateFlow()
    fun setToastEvent(event : ShowToastEvent?) {
        _toastEvent.value = event
    }
    fun triggerToast(message: String) {
        _toastEvent.value = ShowToastEvent(message)
    }

    //progress bar
    var _progressBarVisible = mutableStateOf(false)
    val progressBarVisible: MutableState<Boolean> get() = _progressBarVisible
    fun setProgressBarVisible(visible: Boolean) {
        _progressBarVisible.value = visible
    }
}