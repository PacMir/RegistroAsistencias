package es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.SesionActividad
import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.SesionActividadRepository
import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.SesionRepository
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateUtil
import kotlinx.coroutines.flow.Flow
import java.util.*

class SesionViewModel : ViewModel() {
    private val saRepository: SesionActividadRepository
    private val sRepository: SesionRepository
    private val todayDate: Date = DateUtil.currentTimestampDate()
    private val userEmail: String
    private val db = App.getInstance()

    init {
        saRepository = SesionActividadRepository(db.sesionActividadDao())
        sRepository = SesionRepository(db.sesionDao())
        userEmail = Firebase.auth.currentUser!!.email.toString()
    }

    // Sesiones del día de hoy
    fun getToday(): Flow<List<SesionActividad>> = saRepository.getToday(userEmail, todayDate)

    // Sesiones anteriores
    fun getPast(): Flow<List<SesionActividad>> = saRepository.getPast(userEmail, todayDate)

    fun insertToday() {
        val now = Date()
        val now2hours = now.time.plus(7200000)
        sRepository.save(Sesion("0308.22", now, Date(now2hours), null, null))
    }
}