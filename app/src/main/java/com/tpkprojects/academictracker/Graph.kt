package com.tpkprojects.academictracker

import android.content.Context
import androidx.room.Room
import com.tpkprojects.academictracker.dataModel.AcademicDatabase
import com.tpkprojects.academictracker.dataModel.AcademicRepository

object Graph{

    lateinit var database: AcademicDatabase

    val academicRepository by lazy{
        AcademicRepository(database.studentDao(), database.subjectDao(), database.testDao(), database.academicDao())
    }

    fun provide(context: Context){
        database = Room.databaseBuilder(context, AcademicDatabase::class.java, "academic-database.db").build()
    }
}