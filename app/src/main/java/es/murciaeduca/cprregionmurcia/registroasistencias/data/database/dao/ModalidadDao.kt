package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.ModalidadEntity

@Dao
interface ModalidadDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun populateData(modalidades: List<ModalidadEntity>)

    @Query("SELECT * FROM modalidades WHERE mod_id = :modalidad_id")
    fun getById(modalidad_id: Int): ModalidadEntity
}