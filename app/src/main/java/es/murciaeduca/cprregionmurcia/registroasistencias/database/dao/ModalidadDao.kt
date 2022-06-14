package es.murciaeduca.cprregionmurcia.registroasistencias.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.database.entities.Modalidad

@Dao
interface ModalidadDao {
    @Insert
    fun save(modalidad: Modalidad)
    @Update
    fun update(modalidad: Modalidad)
    @Delete
    fun delete(modalidad: Modalidad)

    @Query("SELECT * FROM modalidades WHERE mod_id = :modalidad_id")
    fun getById(modalidad_id: Int): Modalidad
}