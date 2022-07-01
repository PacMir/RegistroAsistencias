package es.murciaeduca.cprregionmurcia.registroasistencias.data.database

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Clase para el RecyclerView de sesiones
 */
@Parcelize
data class SesionActividad(
    val id: Long,
    val modalidad: String,
    val codigo: String,
    val titulo: String,
    val responsable: String,
    val inicio: Date,
    val fin: Date,
    val num_participantes: Int,
    val num_asisten: Int,
    val upload: Date?
) : Parcelable