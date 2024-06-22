package com.tpkprojects.academictracker.dataModel

import java.time.LocalDate

val converter = Converters()

val usr = User("069", "dummy", "abc@xyz.com" )

object sbj {
    val subjectList = listOf(
        Subject("069-English", "069", "English"),
        Subject("069-Maths", "069", "Maths")
    )
}

object tst{
//    val testList = listOf(
//        Test(0, "069-English", "Eng1", converter.fromString("01-12-2021")?:LocalDate.now(), 100  ,75 ),
//        Test(1, "069-English", "Eng2", converter.fromString("02-12-2021")?:LocalDate.now(), 100  ,25 ),
//        Test(2, "069-Maths", "Maths1", converter.fromString("03-12-2021")?:LocalDate.now(), 100  ,50 ),
//        Test(3, "069-Maths", "Maths2", converter.fromString("04-12-2021")?:LocalDate.now(), 50  ,50 )
//    )
}