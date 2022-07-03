package es.murciaeduca.cprregionmurcia.registroasistencias.data.repo

import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.SesionDao
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion

class SesionRepository(private val dao: SesionDao) {
    suspend fun getById(sesion_id: Long) = dao.getById(sesion_id)
    suspend fun save(sesion: Sesion) = dao.save(sesion)

    /**
     * Listados de sesiones del día y anteriores
     */
    fun getPast(email: String) = dao.getPast(email)
    fun getCountPast(email: String) = dao.getCountPast(email)
    fun getToday(email: String) = dao.getToday(email)
    fun getCountToday(email: String) = dao.getCountToday(email)

    /**
     * Live data de sesiones pasadas pendientes de enviar
     */
    fun getNotSent(email: String) = dao.getNotSent(email)

    /**
     * TODO A falta de implementar el envío de datos
     */
    suspend fun send(sesion_id: Long) = dao.send(sesion_id)
}