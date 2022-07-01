package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Actividad

@Dao
interface ActividadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(actividad: Actividad)

    @Update
    suspend fun update(actividad: Actividad)

    @Delete
    suspend fun delete(actividad: Actividad)

    @Query("SELECT * FROM actividades WHERE act_codigo = :codigo")
    suspend fun getById(codigo: String): Actividad

    @Query("DELETE FROM actividades WHERE user_email = :email")
    suspend fun deleteAllFromUser(email: String)
}