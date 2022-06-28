package es.murciaeduca.cprregionmurcia.registroasistencias.data.repo

import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.ParticipanteActividadDao
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.ParticipanteActividad
import kotlinx.coroutines.flow.Flow
import java.util.*

class ParticipanteActividadRepository(private val dao: ParticipanteActividadDao) {
    fun getAllInActividad(act_codigo: String, ses_inicio: Date): Flow<List<ParticipanteActividad>> =
        dao.getAllInActividad(act_codigo, ses_inicio)
}