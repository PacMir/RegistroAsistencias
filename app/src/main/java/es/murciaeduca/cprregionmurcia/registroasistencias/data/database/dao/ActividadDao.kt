package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Actividad

@Dao
interface ActividadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(actividad: Actividad)

    @Update
    fun update(actividad: Actividad)

    @Delete
    fun delete(actividad: Actividad)

    @Query("SELECT * FROM actividades WHERE user_email = :email")
    fun getAllFromUser(email: String): List<Actividad>

    @Query("SELECT * FROM actividades WHERE act_codigo = :codigo")
    fun getById(codigo: String): Actividad

    @Query("DELETE FROM actividades WHERE user_email = :email")
    fun deleteAllFromUser(email: String)
}