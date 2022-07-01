package es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.ParticipanteAsistencia
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Asistencia
import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.AsistenciaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class AsistenciaViewModel : ViewModel() {
    private val repository = AsistenciaRepository(App.getInstance().asistenciaDao())
    private val todayDate = Date(System.currentTimeMillis())

    fun save(sesion_id: Long, part_id: Long) = viewModelScope.launch {
        repository.save(Asistencia(0, sesion_id, part_id, 1.0F, 0, todayDate))
    }

    fun delete(asistencia: Asistencia) = viewModelScope.launch {
        repository.delete(asistencia)
    }

    fun deleteById(sesion_id: Long, part_id: Long) = viewModelScope.launch {
        repository.deleteById(sesion_id, part_id)
    }

    fun getAsisten(sesion_id: Long) = repository.getAsisten(sesion_id)

    fun saveFromQr(p: ParticipanteAsistencia, sesion_id: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.save(Asistencia(0, sesion_id, p.id, 1.0F, 1, todayDate))
    }
}