package es.murciaeduca.cprregionmurcia.registroasistencias.data.repo

import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.SesionDao
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion

class SesionRepository(private val dao: SesionDao) {
    fun save(sesion: Sesion) = dao.save(sesion)
}