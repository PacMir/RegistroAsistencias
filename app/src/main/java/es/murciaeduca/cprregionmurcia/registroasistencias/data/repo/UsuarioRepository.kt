package es.murciaeduca.cprregionmurcia.registroasistencias.data.repo

import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.UsuarioDao
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.UsuarioEntity

class UsuarioRepository {
    private val userDao: UsuarioDao = App.getInstance().usuarioDao()

    suspend fun save(usuario: UsuarioEntity) = userDao.save(usuario)
    suspend fun getLongName(email: String): String {
        return userDao.getLongName(email)
    }
}
