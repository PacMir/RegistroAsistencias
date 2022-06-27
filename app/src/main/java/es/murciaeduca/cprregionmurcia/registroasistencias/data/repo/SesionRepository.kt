package es.murciaeduca.cprregionmurcia.registroasistencias.data.repo

import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.SesionDao
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion
import java.util.*

class SesionRepository(private val dao: SesionDao) {
    fun getPast(now: Date): List<Sesion> = dao.getPast(now)
    fun getToday(now: Date): List<Sesion> = dao.getToday(now)
}