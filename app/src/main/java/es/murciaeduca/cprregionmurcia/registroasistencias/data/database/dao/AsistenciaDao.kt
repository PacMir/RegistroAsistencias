package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.ParticipanteAsistencia
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Asistencia

@Dao
interface AsistenciaDao {
    @Insert
    suspend fun save(asistencia: Asistencia)

    @Update
    suspend fun update(asistencia: Asistencia)

    @Delete
    suspend fun delete(asistencia: Asistencia)

    @Query("DELETE FROM asistencias WHERE ses_id = :sesion_id AND part_id = :part_id")
    suspend fun deleteById(sesion_id: Long, part_id: Long)

    @Query("SELECT COUNT(*) FROM asistencias a INNER JOIN participantes p ON a.part_id = p.part_id WHERE p.part_nif = :part_nif AND ses_id = :sesion_id AND marca_temporal IS NOT NULL")
    suspend fun checkAsistencia(part_nif: String, sesion_id: Long) : Int

    @Query("SELECT COUNT(*) FROM asistencias WHERE ses_id = :sesion_id")
    fun getAsisten(sesion_id: Long) : LiveData<Int>

    /**
     * Devuelve el ID de un participante si se puede marcar asistencia
     */
    @Query("SELECT a.part_id AS id, p.part_nif AS nif, p.part_nombre AS nombre, p.part_apellidos AS apellidos, p.act_codigo AS actividad_codigo, " +
            "s.ses_fin AS sesion_fin, a.marca_temporal AS asistencia, a.tipo_registro " +
            "FROM asistencias a INNER JOIN participantes p ON a.part_id = p.part_id " +
            "INNER JOIN sesiones s ON a.ses_id = s.ses_id " +
            "WHERE p.act_codigo = :act_codigo AND p.part_nif = :part_nif AND s.ses_id = :sesion_id")
    fun checkAsistencia(act_codigo: String, part_nif: String, sesion_id: Long) : ParticipanteAsistencia
}





