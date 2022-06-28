package es.murciaeduca.cprregionmurcia.registroasistencias.util

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    @TypeConverter
    fun timestampToDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }
}