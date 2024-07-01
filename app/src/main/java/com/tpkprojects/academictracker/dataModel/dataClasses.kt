package com.tpkprojects.academictracker.dataModel

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDate

data class ShortTestData(
    val thisTestDate: LocalDate,
    val obtainedMarks: Int,
    val maxMarks: Int
)

class LocalDateAsStringAdapter : TypeAdapter<LocalDate>() {

    val converter = Converters()
    override fun write(out: JsonWriter, value: LocalDate?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(converter.dateToString(value))
        }
    }

    override fun read(reader: JsonReader): LocalDate? {
        return if (reader.peek() == com.google.gson.stream.JsonToken.NULL) {
            reader.nextNull()
            null
        } else {
            converter.fromString( reader.nextString() )
        }
    }
}