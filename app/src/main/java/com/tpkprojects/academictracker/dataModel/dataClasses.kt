package com.tpkprojects.academictracker.dataModel

import java.time.LocalDate

data class ShortTestData(
    val thisTestDate: LocalDate,
    val obtainedMarks: Int,
    val maxMarks: Int
)