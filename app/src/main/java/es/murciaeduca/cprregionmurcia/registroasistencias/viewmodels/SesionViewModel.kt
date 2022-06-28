package es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.ColumnInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.SesionActividad
import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.SesionActividadRepository
import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.SesionRepository
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SesionViewModel : ViewModel() {
    private val saRepository: SesionActividadRepository
    private val sRepository: SesionRepository
    private val _list = MutableLiveData<List<SesionActividad>>()
    private val todayDate: Date = DateUtil.currentTimestampDate()
    private val userEmail: String
    private val db = App.getInstance()

    // Getter _list
    val list: LiveData<List<SesionActividad>> get() = _list

    init {
        saRepository = SesionActividadRepository(db.sesionActividaDao())
        sRepository = SesionRepository(db.sesionDao())
        userEmail = Firebase.auth.currentUser!!.email.toString()
    }

    // Sesiones del día de hoy
    fun getToday() {
        viewModelScope.launch(Dispatchers.IO) {
            _list.postValue(saRepository.getToday(userEmail, todayDate))
        }
    }

    // Sesiones anteriores
    fun getPast() {
        viewModelScope.launch(Dispatchers.IO) {
            _list.postValue(saRepository.getPast(userEmail, todayDate))
        }
    }

    fun insertToday() {
        viewModelScope.launch(Dispatchers.IO) {
            val now = Date()
            val now_2_hours = now.time.plus(7200000)
            val sesion = Sesion("0308.22", now, Date(now_2_hours), null, null)
            sRepository.save(sesion)
        }
    }
}