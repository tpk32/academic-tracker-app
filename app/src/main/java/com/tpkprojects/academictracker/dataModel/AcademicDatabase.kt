package com.tpkprojects.academictracker.dataModel

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class, Subject::class, Test::class], version=1)
@TypeConverters(Converters::class)
abstract class AcademicDatabase : RoomDatabase(){
    abstract fun userDao() : UserDao
    abstract fun subjectDao() : SubjectDao
    abstract fun testDao(): TestDao
    abstract fun academicDao(): AcademicDao
}