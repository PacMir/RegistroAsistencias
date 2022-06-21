package es.murciaeduca.cprregionmurcia.registroasistencias.data.repo

import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.UsuarioDao
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.UsuarioEntity

class TemplateRepository {
    private val userDao: UsuarioDao = App.getInstance().usuarioDao()

    suspend fun save(usuario: UsuarioEntity) = userDao.save(usuario)
    suspend fun getLongName(email: String): String {
        return userDao.getLongName(email)
    }
}


/* Esto va en el archivo viewmodel



    class Factory(private val repository: TemplateRepository) :
        ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UsuarioViewModel(repository) as T
        }
    }

 */


