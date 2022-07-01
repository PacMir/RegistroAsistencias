package es.murciaeduca.cprregionmurcia.registroasistencias.data.database

import java.util.*

/**
 * Clase para participantes y asistencia
 */
data class ParticipanteAsistencia(
    val id: Long,
    val nif: String,
    val nombre: String,
    val apellidos: String,
    val actividad_codigo: String,
    val sesion_fin: Date,
    val asistencia: Date?,
    val tipo_registro: Int?
) {
    override fun toString(): String {
        return "ParticipanteAsistencia(id=$id, nif='$nif', nombre='$nombre', apellidos='$apellidos', actividad_codigo='$actividad_codigo', sesion_fin=$sesion_fin, asistencia=$asistencia, tipo_registro=$tipo_registro)"
    }
}