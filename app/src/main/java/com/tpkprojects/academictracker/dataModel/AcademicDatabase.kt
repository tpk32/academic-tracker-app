package com.tpkprojects.academictracker.dataModel

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Student::class, Subject::class, Test::class], version=1)
@TypeConverters(Converters::class)
abstract class AcademicDatabase : RoomDatabase(){
    abstract fun studentDao() : StudentDao
    abstract fun subjectDao() : SubjectDao
    abstract fun testDao(): TestDao
    abstract fun academicDao(): AcademicDao
}