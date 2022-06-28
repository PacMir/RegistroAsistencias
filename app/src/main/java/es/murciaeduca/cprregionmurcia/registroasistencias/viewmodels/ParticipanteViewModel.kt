package es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels

import androidx.lifecycle.ViewModel
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.ParticipanteActividad
import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.ParticipanteActividadRepository
import kotlinx.coroutines.flow.Flow
import java.util.*

class ParticipanteViewModel : ViewModel() {
    private val paRepository: ParticipanteActividadRepository
    private val db = App.getInstance()

    init{
        paRepository = ParticipanteActividadRepository(db.participanteActividadDao())
    }

    fun getAllInActividad(act_codigo: String, ses_inicio: Date) : Flow<List<ParticipanteActividad>> = paRepository.getAllInActividad(act_codigo, ses_inicio)
}