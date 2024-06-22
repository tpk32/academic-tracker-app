package com.tpkprojects.academictracker

import android.app.Application
import android.view.KeyEvent.DispatcherState
import com.tpkprojects.academictracker.Graph.database
import com.tpkprojects.academictracker.dataModel.AcademicDatabase
import com.tpkprojects.academictracker.dataModel.Converters
import com.tpkprojects.academictracker.dataModel.Subject
import com.tpkprojects.academictracker.dataModel.Test
import com.tpkprojects.academictracker.dataModel.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

class AcademicTrackerApp:Application() {
    override fun onCreate(){
        super.onCreate()
        Graph.provide(this)
        CoroutineScope(Dispatchers.Default).launch {
            //populateDatabase()
        }
    }

// Uncmment following to add data at startup and call it above

//    private suspend fun populateDatabase(){
//        val converter = Converters()
//        val userDao = database.userDao()
//        val subjectDao = database.subjectDao()
//        val testDao = database.testDao()
//        val academicDao = database.academicDao()
//
//        val d_user1 = User("00", "DummyUser", "DummyUser@dummy.com")
//        userDao.insertUser(d_user1)
//
//        val d_subject1 = Subject("00-English", "00", "English")
//        subjectDao.insertSubject(d_subject1)
//        val d_subject2 = Subject("00-Science", "00", "Science")
//        subjectDao.insertSubject(d_subject2)
//
//        val d_test1 = Test(0,"00-English","English", "Eng1", converter.fromString("01-01-2020")?: LocalDate.now(), 100, 50 )
//        testDao.insertTest(d_test1)
//        val d_test2 = Test(1,"00-English","English", "Eng2", converter.fromString("01-01-2020")?: LocalDate.now(), 80, 65 )
//        testDao.insertTest(d_test2)
//        val d_test3 = Test(2,"00-Science","Science", "Sci1", converter.fromString("03-01-2020")?: LocalDate.now(), 100, 82 )
//        testDao.insertTest(d_test3)
//        val d_test4 = Test(3,"00-Science","Science", "Sci2", converter.fromString("05-01-2020")?: LocalDate.now(), 50, 38 )
//        testDao.insertTest(d_test4)
//    }
}