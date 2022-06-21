package es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.UsuarioDao
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.UsuarioEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsuarioViewModel() : ViewModel() {
    private val userDao: UsuarioDao = App.getInstance().usuarioDao()

    fun save(usuario: UsuarioEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                userDao.save(usuario)
            }
        }
    }

    fun getLongName(email: String): String {
        return viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userDao.getLongName(email)
            }
        }.toString()
    }
}