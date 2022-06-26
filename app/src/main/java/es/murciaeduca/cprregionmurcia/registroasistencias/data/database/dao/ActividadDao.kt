package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.ActividadEntity

@Dao
interface ActividadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(actividad: ActividadEntity)

    @Update
    fun update(actividad: ActividadEntity)

    @Delete
    fun delete(actividad: ActividadEntity)

    @Query("SELECT * FROM actividades WHERE user_email = :email")
    fun getAllFromUser(email: String): List<ActividadEntity>

    @Query("SELECT * FROM actividades WHERE act_codigo = :codigo")
    fun getById(codigo: String): ActividadEntity

    @Query("DELETE FROM actividades WHERE user_email = :email")
    fun deleteAllFromUser(email: String)
}