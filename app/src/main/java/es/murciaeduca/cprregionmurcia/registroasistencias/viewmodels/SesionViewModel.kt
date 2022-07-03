package es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion
import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.SesionRepository
import kotlinx.coroutines.launch
import java.util.*

class SesionViewModel : ViewModel() {
    private val repository = SesionRepository(App.getInstance().sesionDao())
    private val userEmail: String = Firebase.auth.currentUser!!.email.toString()

    /**
     * Sesiones del día de hoy
     */
    fun getToday() = repository.getToday(userEmail)
    fun getCountToday() = repository.getCountToday(userEmail)

    /**
     * Sesiones anteriores
     */
    fun getPast() = repository.getPast(userEmail)
    fun getCountPast() = repository.getCountPast(userEmail)

    /**
     * Sesiones pasadas pendientes de enviar (notificación en bottombar)
     */
    fun getNotSent() = repository.getNotSent(userEmail)

    /**
     * TODO Inserción a falta de implementar la descarga de datos
     */
    fun insertToday() = viewModelScope.launch {
        val now = Date(System.currentTimeMillis() - 3600000)
        val now2hours = Date(System.currentTimeMillis() + 7200000)
        repository.save(Sesion(0, "0308.22", now, now2hours, null, null))
    }

    fun send(sesion_id: Long) = viewModelScope.launch {
        repository.send(sesion_id)
    }
}