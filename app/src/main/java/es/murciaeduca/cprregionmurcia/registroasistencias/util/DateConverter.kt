package es.murciaeduca.cprregionmurcia.registroasistencias.util

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun date2Timestamp(date: Date) : Long = date.time

    @TypeConverter
    fun timestamp2Date(timestamp: Long) : Date = Date(timestamp)
}