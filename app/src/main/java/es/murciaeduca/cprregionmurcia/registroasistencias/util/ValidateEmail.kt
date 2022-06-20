package es.murciaeduca.cprregionmurcia.registroasistencias.util

import java.util.regex.Pattern

/**
 * Validar un email
 */
fun isValidEmail(str: String): Boolean {
    val emailPattern = Pattern.compile(
        "[a-zA-Z0-9+._%-+]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9]{0,25}" +
                ")+"
    )

    return emailPattern.matcher(str).matches()
}