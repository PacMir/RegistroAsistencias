package es.murciaeduca.cprregionmurcia.registroasistencias.util

import java.text.SimpleDateFormat
import java.util.*


@Suppress("EnumEntryName")
enum class DateFormats(val format: String) {
    dd_MM_YY_HH_mm("dd/MM/YYYY HH:mm"),
    dd_MM_YY("dd/MM/YYYY"),
    HH_mm("HH:mm")
}

object DateUtil {

    fun currentTimestampDate(): Date {
        return Date(System.currentTimeMillis())
    }

    fun nowToString(format: String?): String? {
        return dateToString(currentTimestampDate(), format)
    }

    fun stringToDate(date: String?): Date? {
        if (date == null) return null

        return SimpleDateFormat(DateFormats.dd_MM_YY_HH_mm.format).parse(date)
    }

    fun dateToString(date: Date?, format: String?): String? {
        if (date == null) return null

        val f = format ?: DateFormats.dd_MM_YY_HH_mm.format
        return SimpleDateFormat(f as String?).format(date)
    }

    fun duration(dateInicio: Date, dateFin: Date): String {
        val diff: Float = (dateFin.time - dateInicio.time).toFloat()

        val diffMinutesF = diff / (60 * 1000) % 60
        val diffHoursF = diff / (60 * 60 * 1000) % 24
        val diffMinutes = diffMinutesF.toInt().toString()
        val diffHours = diffHoursF.toInt().toString()

        var output = "${diffHours}h."
        if (diffHoursF % 1.0 != 0.0)
            output += " ${diffMinutes}m."

        return output
    }
}