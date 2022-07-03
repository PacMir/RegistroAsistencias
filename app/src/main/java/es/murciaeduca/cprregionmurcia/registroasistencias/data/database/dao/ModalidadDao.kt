package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Modalidad
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion

@Dao
interface ModalidadDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun populateData(modalidades: List<Modalidad>)
}