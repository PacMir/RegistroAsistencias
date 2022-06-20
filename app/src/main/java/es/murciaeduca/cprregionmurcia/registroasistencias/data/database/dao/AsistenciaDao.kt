package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.AsistenciaEntity

@Dao
interface AsistenciaDao {
    @Insert
    fun save(asistencia: AsistenciaEntity)

    @Update
    fun update(asistencia: AsistenciaEntity)

    @Delete
    fun delete(asistencia: AsistenciaEntity)

    @Query("SELECT * FROM asistencias WHERE ses_id = :sesion_id")
    fun getAllInSesion(sesion_id: Long): List<AsistenciaEntity>
}