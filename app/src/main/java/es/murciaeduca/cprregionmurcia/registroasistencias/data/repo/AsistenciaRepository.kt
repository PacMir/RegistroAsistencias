package es.murciaeduca.cprregionmurcia.registroasistencias.data.repo

import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.AsistenciaDao
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Asistencia

class AsistenciaRepository(private val dao: AsistenciaDao) {
    suspend fun save(asistencia: Asistencia) = dao.save(asistencia)
    suspend fun delete(asistencia: Asistencia) = dao.save(asistencia)
    suspend fun deleteById(sesion_id: Long, part_id: Long) = dao.deleteById(sesion_id, part_id)
    fun getAsisten(sesion_id: Long) = dao.getAsisten(sesion_id)
}