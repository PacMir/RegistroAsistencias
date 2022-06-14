package es.murciaeduca.cprregionmurcia.registroasistencias.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.database.entities.Asistencia

@Dao
interface AsistenciaDao {
    @Insert
    fun save(asistencia: Asistencia)
    @Update
    fun update(asistencia: Asistencia)
    @Delete
    fun delete(asistencia: Asistencia)

    @Query("SELECT * FROM asistencias WHERE ses_id = :sesion_id")
    fun getAllInSesion(sesion_id: Long): List<Asistencia>
}