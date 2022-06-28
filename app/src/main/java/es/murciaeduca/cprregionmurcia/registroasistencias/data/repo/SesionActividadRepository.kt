package es.murciaeduca.cprregionmurcia.registroasistencias.data.repo

import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.SesionActividadDao
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.SesionActividad
import kotlinx.coroutines.flow.Flow
import java.util.*

class SesionActividadRepository(private val dao: SesionActividadDao) {
    fun getPast(email: String, now: Date): Flow<List<SesionActividad>> = dao.getPast(email, now)
    fun getToday(email: String, now: Date): Flow<List<SesionActividad>> = dao.getToday(email, now)
}