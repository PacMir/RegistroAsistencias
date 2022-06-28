package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import java.util.*

data class ParticipanteActividad(
    val nif: String,
    val nombre: String,
    val apellidos: String,
    val actividad_codigo: String,
    val asiste: Date?,
    val tipo_registro: Int?
)
