package es.murciaeduca.cprregionmurcia.registroasistencias.data.repo

import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.SesionDao
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion
import java.util.*

class SesionRepository(private val dao: SesionDao) {
    suspend fun getById(sesion_id: Long) = dao.getById(sesion_id)
    suspend fun save(sesion: Sesion) = dao.save(sesion)

    /**
     * Listados de sesiones del día y anteriores
     */
    fun getPast(email: String, now: Date) = dao.getPast(email, now)
    fun getToday(email: String, start: Long, end: Long) = dao.getToday(email, start, end)

    /**
     * Live data de sesiones pasadas pendientes de enviar
     */
    fun getNotSent(email: String, now: Date) = dao.getNotSent(email, now)

    /**
     * TODO A falta de implementar el envío de datos
     */
    suspend fun send(sesion_id: Long, now: Date) = dao.send(sesion_id, now)
}