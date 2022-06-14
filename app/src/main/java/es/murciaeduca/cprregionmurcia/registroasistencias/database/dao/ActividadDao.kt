package es.murciaeduca.cprregionmurcia.registroasistencias.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.database.entities.Actividad

@Dao
interface ActividadDao {
    @Insert
    fun save(actividad: Actividad)
    @Update
    fun update(actividad: Actividad)
    @Delete
    fun delete(actividad: Actividad)
    @Query("SELECT * FROM actividades")
    fun getAll(): List<Actividad>
    @Query("SELECT * FROM actividades WHERE act_codigo = :codigo")
    fun getById(codigo: String): Actividad
    @Query("DELETE FROM actividades")
    fun deleteAll()
}