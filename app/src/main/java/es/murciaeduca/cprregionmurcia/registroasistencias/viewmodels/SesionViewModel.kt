package es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion
import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.SesionRepository
import es.murciaeduca.cprregionmurcia.registroasistencias.util.AppDateUtil
import kotlinx.coroutines.launch
import java.util.*

class SesionViewModel : ViewModel() {
    private val repository = SesionRepository(App.getInstance().sesionDao())
    private val todayDate : Date = Date(System.currentTimeMillis())
    private val todayStartDate: Long = AppDateUtil.getLongFromToday(null)
    private val todayEndDate: Long = AppDateUtil.getLongFromToday(1)
    private val userEmail: String = Firebase.auth.currentUser!!.email.toString()

    /**
     * Sesiones del día de hoy
     */
    fun getToday() = repository.getToday(userEmail, todayStartDate, todayEndDate)
    fun getCountToday() = repository.getCountToday(userEmail, todayStartDate, todayEndDate)

    /**
     * Sesiones anteriores
     */
    fun getPast() = repository.getPast(userEmail, todayDate)
    fun getCountPast() = repository.getCountPast(userEmail, todayDate)

    /**
     * Sesiones pasadas pendientes de enviar (notificación en bottombar)
     */
    fun getNotSent() = repository.getNotSent(userEmail, todayDate)

    /**
     * TODO Inserción a falta de implementar la descarga de datos
     */
    fun insertToday() = viewModelScope.launch {
        val now = Date()
        val now2hours = now.time.plus(7200000)
        repository.save(Sesion(0, "0308.22", now, Date(now2hours), null, null))
    }

    fun send(sesion_id: Long) = viewModelScope.launch {
        repository.send(sesion_id, todayDate)
    }
}