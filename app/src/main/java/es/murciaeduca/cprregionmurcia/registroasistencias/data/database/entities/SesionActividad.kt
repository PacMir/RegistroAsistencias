package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import java.util.*

data class SesionActividad(
    val modalidad: String,
    val codigo: String,
    val titulo: String,
    val responsable: String,
    val inicio: Date,
    val fin: Date
)