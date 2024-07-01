package com.tpkprojects.academictracker

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tpkprojects.academictracker.dataModel.AcademicRepository
import com.tpkprojects.academictracker.dataModel.Converters
import com.tpkprojects.academictracker.dataModel.ShortTestData
import com.tpkprojects.academictracker.dataModel.Subject
import com.tpkprojects.academictracker.dataModel.Test
import com.tpkprojects.academictracker.dataModel.Student
import com.tpkprojects.academictracker.dataModel.TestWithSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel(
    private val academicRepository: AcademicRepository = Graph.academicRepository
): ViewModel() {

    val converter = Converters()
    val _firstView = mutableStateOf(0)
    var firstView: MutableState<Int> = _firstView
    fun setFirstView(index: Int) {
        _firstView.value = index
    }

    fun deleteUser(){
        viewModelScope.launch(){
            academicRepository.deleteStudent(student.value!!)
        }
    }


    //MainView items ----------------------------------------------------------->

    private var _currentScreen: MutableState<String> = mutableStateOf("")
    val currentScreen: MutableState<String>
        get() = _currentScreen
    fun setCurrentScreen(screenRoute: String) {
        _currentScreen.value = screenRoute
    }

    private val _currentSubject: MutableState<String> = mutableStateOf("Home")
    val currentSubject: MutableState<String>
        get() = _currentSubject
    fun setCurrentSubject(subject: String) {
        _currentSubject.value = subject
    }

    private val _currentTitle: MutableState<String> = mutableStateOf("Home")
    val currentTitle: MutableState<String>
        get() = _currentTitle
    fun setCurrentTitle(title: String) {
        _currentTitle.value = title
    }

    var accountExpanded = mutableStateOf(false)
    var userAlertDialog = mutableStateOf(false)
    var subjectAlertDialog = mutableStateOf(false)

    // HomeView items ---------------------------------------------------.

    private var _student = mutableStateOf<Student?>(null)
    val student: MutableState<Student?> get() = _student
    fun getStudent(){
        viewModelScope.launch {
            _student.value = academicRepository.getStudent()
        }
    }
    fun setStudent(student:Student?){
        _student.value = student
    }

    // this adds the user and gets its value in one goadd
    fun addStudent(student:Student){
        viewModelScope.launch(){
            academicRepository.addStudent(student)
            _student.value = academicRepository.getStudent()
            Log.d("usermsg", "${_student.value!!}")
            initialiseModel()
            setFirstView(2)
        }
    }

    private var _averagePercentage : Flow<Double?> = emptyFlow()
    val averagePercentage: Flow<Double?> get() = _averagePercentage
    fun getAveragePercentage(userId: String){
        viewModelScope.launch {
            _averagePercentage = academicRepository.getAveragePercentage(userId)
        }
    }

    private var _totalTestCount: Flow<Int?> = emptyFlow()
    val totalTestCount: Flow<Int?> get() = _totalTestCount
    fun getTestCount(userId: String){
        viewModelScope.launch {
            _totalTestCount = academicRepository.getTestCount(userId)
        }
    }

    private var _mostPracticedSubject : Flow<String?> = emptyFlow()
    val mostPracticedSubject: Flow<String?> get() = _mostPracticedSubject
    fun getSubjectWithMostTests(userId: String){
        viewModelScope.launch{
            _mostPracticedSubject = academicRepository.getSubjectWithMostTests(userId)
        }
    }

    private var _latestTestDate : Flow<LocalDate?> = emptyFlow()
    val latestTestDate: Flow<LocalDate?> get() = _latestTestDate
    fun getLatestTestDate(userId: String) {
        viewModelScope.launch {
            _latestTestDate = academicRepository.getLatestTestDate(userId)
        }
    }


    //SubjectView Items------------------------------------------------------------>

    private var _subjectPercentage : Flow<Double?> = emptyFlow()
    val subjectPercentage: Flow<Double?> get() = _subjectPercentage
    fun getAvgPercentageInSubject(subjectId: String){
        viewModelScope.launch {
            _subjectPercentage = academicRepository.getSubjectPercentage(student.value!!.studentId, subjectId)
        }
    }

    private var _testCountForSubject : Flow<Int?> = emptyFlow()
    val testCountForSubject: Flow<Int?> get() = _testCountForSubject
    fun getTestCountForSubject(subjectId: String){
        viewModelScope.launch {
            _testCountForSubject= academicRepository.getTestCountForSubject(student.value!!.studentId, subjectId)
        }
    }

    private var _subjectLastTestData : Flow<ShortTestData?> = emptyFlow()
    val subjectLastTestData: Flow<ShortTestData?> get() = _subjectLastTestData
    fun getSubjectLastTestData(subjectId: String){
        viewModelScope.launch {
            _subjectLastTestData = academicRepository.getSubjectLastTestData(student.value!!.studentId, subjectId)
        }
    }

    private var _subjectFirstTestData: Flow<ShortTestData?> = emptyFlow()
    val subjectFirstTestData: Flow<ShortTestData?> get() = _subjectFirstTestData
    fun getSubjectFirstTestData(subjectId: String){
        viewModelScope.launch {
            _subjectFirstTestData = academicRepository.getSubjectFirstTestData(student.value!!.studentId, subjectId)
        }
    }


    //TestView Items---------------------------------------------------------------->

    var allTestsWithSubject : Flow<List<TestWithSubject> > = emptyFlow()
    fun getAllTests(studentId: String) {
        viewModelScope.launch {
            allTestsWithSubject = academicRepository.getAllTests(studentId)
        }
    }

    var testsById: Flow<List<Test>> = emptyFlow()
    fun getTestsBySubjectName(subjectName: String){
        viewModelScope.launch(){
            testsById = academicRepository.getTestsBySubjectName(student.value!!.studentId,subjectName)
        }
    }

    var _selectedTestForDelete = mutableStateOf<Test?>(null)
    val selectedTestforDelete: MutableState<Test?> get() = _selectedTestForDelete
    fun setSelectedTestForDelete(test: Test?){
        _selectedTestForDelete.value = test
    }
    fun deleteTest(test: Test = _selectedTestForDelete.value!!){
        viewModelScope.launch(){
            val subjectName = academicRepository.getSubjectNameBySubjectId(student.value!!.studentId, test.subjectId)
            academicRepository.deleteTest(student.value!!.studentId, subjectName!!, test)
        }
    }

    //Drawer Items --------------------------------------------------------------->


    var getSubjectsByStudentId: Flow<List<Subject>> = flowOf(listOf())
    fun getSubjectsByStudentId(){
        viewModelScope.launch {
            getSubjectsByStudentId = academicRepository.getSubjectsByStudentId(student.value!!.studentId)
        }
    }

    var _selectedSubjectForDelete = mutableStateOf<Subject?>(null)
    val selectedSubjectForDelete: MutableState<Subject?> get() = _selectedSubjectForDelete
    fun setSelectedSubjectForDelete(subject: Subject?){
        _selectedSubjectForDelete.value = subject
    }
    fun deleteSubject(subject: Subject = selectedSubjectForDelete.value!!){
        viewModelScope.launch(){
            academicRepository.deleteSubject(student.value!!.studentId, subject)
        }
    }


    //AddTestDailogViewitems ----------------------------------------------------->

    val addTestDialogOpen =  mutableStateOf(false)

    var addTestDialogTTState = mutableStateOf("")
    var addTestDialogMOState = mutableStateOf("")
    var addTestDialogMMState = mutableStateOf("")
    var pickedDate = mutableStateOf(LocalDate.now().plusDays(10))
    var addTestDialogTDState = derivedStateOf {
        if(pickedDate.value == LocalDate.now().plusDays(10)) {
            converter.dateToString(null)
        }
        else converter.dateToString(pickedDate.value)

    }


    fun onAddTestDialogTTChange(text: String) {
        addTestDialogTTState.value = text
    }
    fun onAddTestDialogMOChange(text: String) {
        addTestDialogMOState.value = text
    }
    fun onAddTestDialogMMChange(text: String) {
        addTestDialogMMState.value = text
    }
    fun onAddTestDialogTDChange(date: LocalDate) {
        pickedDate.value = date
    }

    var subjectSelectedOption = mutableStateOf("")
    fun resetSubjectSelectedOption() {
        subjectSelectedOption.value = ""
    }

    var subjectExpanded = mutableStateOf(false)
    fun resetSubjectExpanded() {
        subjectExpanded.value = false
    }

    fun resetAddTestDialog(){
        resetSubjectSelectedOption()
        resetSubjectExpanded()
        addTestDialogTTState.value = ""
        addTestDialogMMState.value = ""
        addTestDialogMOState.value = ""
        pickedDate.value = LocalDate.now().plusDays(10)
    }

    private var _addTestResponse:MutableState<Long> = mutableStateOf(-10L)
    val addTestResponse: MutableState<Long> get() = _addTestResponse
    fun addTest(callback: (Long)->Unit): Long{
        val subjectName = subjectSelectedOption.value
        setProgressBarVisible(true)
        viewModelScope.launch(Dispatchers.IO){
            val test = Test(
                testName = addTestDialogTTState.value,
                testDate = pickedDate.value,
                maxMarks = addTestDialogMMState.value.toInt(),
                obtainedMarks = addTestDialogMOState.value.toInt() ,
                subjectId = academicRepository.getSubjectIdBySubjectName(student.value!!.studentId, subjectName)!!
            )
            val response = academicRepository.addTest(test)
            callback(response)
            setProgressBarVisible(false)
        }
        return addTestResponse.value
    }


    // Progress Bar items ---------------------------------------------------------->

    var _progressBarVisible = mutableStateOf(false)
    val progressBarVisible: MutableState<Boolean> get() = _progressBarVisible
    fun setProgressBarVisible(visible: Boolean) {
        _progressBarVisible.value = visible
    }


    // AddSubjectDialogView items -------------------------------------------------->

    var addSubjectDialogOpen = mutableStateOf(false)
    var addSubjectDialogTFState = mutableStateOf("")
    fun onAddSubjectDialogTFChange(text: String) {
        addSubjectDialogTFState.value = text
    }

    private var _addSubjectResponse:MutableState<Long> = mutableStateOf(-10L)
    val addSubjectResponse: MutableState<Long> get() = _addSubjectResponse

    fun addSubject(callback: (Long)->Unit){
        val subject = Subject(
            studentId = (student.value!!.studentId),
            subjectName = addSubjectDialogTFState.value
        )
        _progressBarVisible.value = true
        viewModelScope.launch(){
            val response = academicRepository.addSubject(subject)
            callback(response)
            setProgressBarVisible(false)
        }
    }


    // TestView items--------------------------------------------------------------->

    var testAlertDialog = mutableStateOf(false)


    // SnackBar items -------------------------------------------------------------->
    val snackbarHostState = SnackbarHostState()
    var _snackbarEvent = MutableStateFlow<ShowSnackbarEvent?>(null)
    val snackbarEvent = _snackbarEvent.asStateFlow()
    fun setSnackbarEvent(event : ShowSnackbarEvent?) {
        _snackbarEvent.value = event
    }
    fun triggerSnackBar(message: String, actionLabel: String? = null) {
        _snackbarEvent.value = ShowSnackbarEvent(message, actionLabel)
    }


    // Toast Items-------------------------------------------------------------------->
    var _toastEvent = MutableStateFlow<ShowToastEvent?>(null)
    val toastEvent = _toastEvent.asStateFlow()
    fun setToastEvent(event : ShowToastEvent?) {
        _toastEvent.value = event
    }
    fun triggerToast(message: String) {
        _toastEvent.value = ShowToastEvent(message)
    }


    //Flow items -------------------------------------------------------------------->

    fun initialiseModel(){
        getSubjectsByStudentId()
        getAllTests(student.value!!.studentId)
        getAveragePercentage(student.value!!.studentId)
        getTestCount(student.value!!.studentId)
        getSubjectWithMostTests(student.value!!.studentId)
        getLatestTestDate(student.value!!.studentId)
    }
}