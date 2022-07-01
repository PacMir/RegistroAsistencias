package es.murciaeduca.cprregionmurcia.registroasistencias.util

import java.text.SimpleDateFormat
import java.util.*


@Suppress("EnumEntryName")
enum class DateFormats(val format: String) {
    DATE_TIME("dd/MM/yyyy HH:mm"),
    DATE("dd/MM/yyyy"),
    TIME("HH:mm"),
    DATE_TEXT("dd MMMM"),
    LONG_DATE_TEXT("EEEE',' d 'de' MMMM")
}

object AppDateUtil {

    /**
     * Genera el texto de información de fecha de una sesión
     */
    fun generateDateInfo(dateInicio: Date, dateFin: Date): String {
        val fecha = dateToString(dateInicio, DateFormats.LONG_DATE_TEXT.format)
        val horario = dateToString(
            dateInicio, DateFormats.TIME.format) + "h" + " a " +
                dateToString(dateFin, DateFormats.TIME.format
                ) + "h"

        return "${fecha}. De $horario"
    }

    /**
     * Pasa un Date a un String
     * Puede aportarse un formato de los definidos en DateFormats
     */
    fun dateToString(date: Date?, format: String?): String? {
        if (date == null) return null
        val locale = Locale("ES", "es")
        val f = format ?: DateFormats.DATE_TIME.format
        return SimpleDateFormat(f as String?, locale).format(date)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    /**
     * Devuelve la diferencia en horas y minutos en un periodo de tiempo
     */
    fun duration(dateInicio: Date, dateFin: Date): String {
        val diff: Float = (dateFin.time - dateInicio.time).toFloat()

        val diffMinutesF = diff / (60 * 1000) % 60
        val diffHoursF = diff / (60 * 60 * 1000) % 24
        val diffMinutes = diffMinutesF.toInt().toString()
        val diffHours = diffHoursF.toInt().toString()

        var output = "${diffHours}h"
        if (diffHoursF % 1.0 != 0.0)
            output += "${diffMinutes}m"

        return output
    }

    /**
     * Obtiene milisegundos de la fecha actual
     * Si se proporciona días, se suman a esta
     */
    fun getLongFromToday(day: Int?): Long {
        var millis = System.currentTimeMillis()
        if (day != null) {
            millis += (86400000 * day)
        }

        val locale = Locale("ES", "es")
        val currentDay = dateToString(Date(millis), DateFormats.DATE.format)
        val parseDate =
            currentDay?.let { SimpleDateFormat(DateFormats.DATE.format, locale).parse(it) }
        return parseDate!!.time
    }
}