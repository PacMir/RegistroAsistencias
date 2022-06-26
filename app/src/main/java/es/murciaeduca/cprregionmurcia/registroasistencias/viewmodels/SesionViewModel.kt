package es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.SesionEntity
import es.murciaeduca.cprregionmurcia.registroasistencias.data.repo.SesionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SesionViewModel() : ViewModel() {
    private val repository: SesionRepository

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>get() = _loading

    private val _sesiones = MutableLiveData<List<SesionEntity>>(emptyList())
    val sesiones: LiveData<List<SesionEntity>>get() = _sesiones

    val sesionList = MutableLiveData<List<SesionEntity>>()
    var params = MutableLiveData<Date>()

    init{
        val dao = App.getInstance().sesionDao()
        repository = SesionRepository(dao)

        _loading.value = true
    }

    private fun getToday(date: Date){
        viewModelScope.launch(Dispatchers.IO){
            _sesiones.value = repository.getToday(date)
        }
    }

    /*
    fun insert(sesionEntity: SesionEntity) = viewModelScope.launch(Dispatchers.IO){
        repository.getToday()
    }
     */
}