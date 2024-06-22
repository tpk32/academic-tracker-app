package com.tpkprojects.academictracker.dataModel

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class Converters {
   // private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @TypeConverter
    fun fromString(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, dateFormat) }
    }

    @TypeConverter
    fun dateToString(date: LocalDate?): String? {
        return date?.let { dateFormat.format(it) }
    }
}

class myConverter {
    private val myDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    fun dateToString(date: LocalDate?): String? {
        return date?.let { myDateFormat.format(it) }
    }
}