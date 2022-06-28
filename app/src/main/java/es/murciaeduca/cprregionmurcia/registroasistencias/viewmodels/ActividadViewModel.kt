package es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels

import androidx.lifecycle.ViewModel
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.ActividadRepository

class ActividadViewModel : ViewModel() {
    private val repository = ActividadRepository(App.getInstance().actividadDao())

    fun getById(codigo: String) = repository.getById(codigo)
}