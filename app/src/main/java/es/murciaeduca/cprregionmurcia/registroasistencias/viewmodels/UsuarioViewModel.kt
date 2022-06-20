package es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.UsuarioEntity
import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {
    fun save(usuario: UsuarioEntity) {
        viewModelScope.launch {
            repository.save(usuario)
        }
    }

    fun getLongName(email: String): String {
        return viewModelScope.launch {
            repository.getLongName(email)
        }.toString()
    }

    class Factory(private val repository: UsuarioRepository) :
        ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UsuarioViewModel(repository) as T
        }
    }
}