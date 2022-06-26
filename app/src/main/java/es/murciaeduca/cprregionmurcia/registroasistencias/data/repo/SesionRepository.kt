package es.murciaeduca.cprregionmurcia.registroasistencias.data.repo

import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.SesionDao
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.SesionEntity
import java.util.*
import kotlinx.coroutines.flow.Flow

class SesionRepository(private val dao: SesionDao) {
    fun getPast(now: Date): Flow<List<SesionEntity>> = dao.getPast(now)
    fun getToday(now: Date): Flow<List<SesionEntity>> = dao.getToday(now)
}