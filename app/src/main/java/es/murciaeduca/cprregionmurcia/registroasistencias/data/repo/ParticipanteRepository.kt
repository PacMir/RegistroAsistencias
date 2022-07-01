package es.murciaeduca.cprregionmurcia.registroasistencias.data.repo

import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.ParticipanteDao
import java.util.*

class ParticipanteRepository(private val dao: ParticipanteDao) {
    fun getAllWithAsistencia(act_codigo: String, ses_id: Long) = dao.getAllWithAsistencia(act_codigo, ses_id)
    fun getWithAsistencia(act_codigo: String, part_nif: String, sesion_id: Long, now: Date) = dao.getWithAsistencia(act_codigo, part_nif, sesion_id, now)
}