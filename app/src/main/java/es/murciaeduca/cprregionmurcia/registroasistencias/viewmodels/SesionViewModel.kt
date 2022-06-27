package es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.SesionActividad
import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.SesionRepository
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SesionViewModel : ViewModel() {
    private val repository: SesionRepository
    private val _list = MutableLiveData<List<SesionActividad>>()
    private val todayDate: Date = DateUtil.currentTimestampDate()
    private val userEmail: String

    // Getter _list
    val list: LiveData<List<SesionActividad>> get() = _list

    init {
        val dao = App.getInstance().sesionActividaDao()
        repository = SesionRepository(dao)
        userEmail = Firebase.auth.currentUser!!.email.toString()
    }

    // Sesiones del día de hoy
    fun getToday() {
        viewModelScope.launch(Dispatchers.IO) {
            _list.postValue(repository.getToday(userEmail, todayDate))
        }
    }

    // Sesiones anteriores
    fun getPast() {
        viewModelScope.launch(Dispatchers.IO) {
            _list.postValue(repository.getPast(userEmail, todayDate))
        }
    }
}