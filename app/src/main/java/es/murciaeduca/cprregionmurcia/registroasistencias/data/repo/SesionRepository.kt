package es.murciaeduca.cprregionmurcia.registroasistencias.data.repo

import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.SesionDao

class SesionRepository {
    private val sesionDao: SesionDao = App.getInstance().sesionDao()

    suspend fun getAll(email: String) {
        val response = sesionDao.getAll()
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


