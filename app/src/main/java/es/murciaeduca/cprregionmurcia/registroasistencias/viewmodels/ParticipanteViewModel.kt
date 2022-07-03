package es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels

import androidx.lifecycle.ViewModel
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.ParticipanteRepository

class ParticipanteViewModel : ViewModel() {
    private val repository = ParticipanteRepository(App.getInstance().participanteDao())

    /**
     * Listado de Participantes de una actividad indicando asistencia
     */
    fun getAllWithAsistencia(act_codigo: String, ses_id: Long) =
        repository.getAllWithAsistencia(act_codigo, ses_id)

    /**
     *  Comprueba un participante
     */
    fun getWithAsistencia(act_codigo: String, part_nif: String, sesion_id: Long) = repository.getWithAsistencia(act_codigo, part_nif, sesion_id)
}