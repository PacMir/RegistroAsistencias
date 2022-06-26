package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.AsistenciaEntity
import java.util.*

@Dao
interface AsistenciaDao {
    @Insert
    fun save(asistencia: AsistenciaEntity)

    @Update
    fun update(asistencia: AsistenciaEntity)

    @Delete
    fun delete(asistencia: AsistenciaEntity)

    @Query("SELECT * FROM asistencias WHERE act_codigo = :codigo AND ses_inicio = :inicio")
    fun getAllInSesion(codigo: String, inicio: Date): List<AsistenciaEntity>
}