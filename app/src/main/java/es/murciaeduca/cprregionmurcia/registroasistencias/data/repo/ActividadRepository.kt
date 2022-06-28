package es.murciaeduca.cprregionmurcia.registroasistencias.data.repo

import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.ActividadDao

class ActividadRepository(private val dao : ActividadDao) {
    fun getById(codigo: String) = dao.getById(codigo)
}