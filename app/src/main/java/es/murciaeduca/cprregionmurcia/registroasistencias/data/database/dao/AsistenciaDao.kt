package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Asistencia
import java.util.*

@Dao
interface AsistenciaDao {
    @Insert
    fun save(asistencia: Asistencia)

    @Update
    fun update(asistencia: Asistencia)

    @Delete
    fun delete(asistencia: Asistencia)

    @Query("SELECT * FROM asistencias WHERE act_codigo = :codigo AND ses_inicio = :inicio")
    fun getAllInSesion(codigo: String, inicio: Date): List<Asistencia>
}