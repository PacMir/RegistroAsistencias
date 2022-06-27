package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Modalidad

@Dao
interface ModalidadDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun populateData(modalidades: List<Modalidad>)

    @Query("SELECT * FROM modalidades WHERE mod_codigo = :codigo")
    fun getById(codigo: String): Modalidad
}