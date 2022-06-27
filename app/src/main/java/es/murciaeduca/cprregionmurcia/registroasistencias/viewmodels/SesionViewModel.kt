package es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels

import androidx.lifecycle.*
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion
import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.SesionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.*

class SesionViewModel : ViewModel() {
    private val repository: SesionRepository
    private val _list = MutableLiveData<List<Sesion>>(emptyList())
    val list: LiveData<List<Sesion>> get() = _list

    init {
        val dao = App.getInstance().sesionDao()
        repository = SesionRepository(dao)
    }

    fun getToday() = viewModelScope.launch(Dispatchers.IO) {
        val timestamp = Timestamp(System.currentTimeMillis())
        _list.value = repository.getToday(Date(timestamp.time))
    }
}