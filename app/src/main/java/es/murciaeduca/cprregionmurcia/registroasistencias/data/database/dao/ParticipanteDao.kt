package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.ParticipanteAsistencia
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Participante
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ParticipanteDao {
    @Insert
    suspend fun save(participante: Participante)

    @Update
    suspend fun update(participante: Participante)

    @Delete
    suspend fun delete(participante: Participante)

    @Query("SELECT * FROM participantes WHERE act_codigo = :codigo")
    fun getAll(codigo: String): Flow<List<Participante>>

    /**
     * Lista los participantes de una actividad con expresión de su asistencia
     */
    @Query(
        "SELECT p.part_id AS id, p.part_nif AS nif, p.part_nombre AS nombre, p.part_apellidos AS apellidos, p.act_codigo AS actividad_codigo, " +
                "t.marca_temporal AS asistencia, t.tipo_registro AS tipo_registro, " +
                "s.ses_fin AS sesion_fin " +
                "FROM participantes p " +
                "INNER JOIN sesiones s ON s.act_codigo = p.act_codigo AND s.ses_id = :ses_id " +
                "LEFT JOIN asistencias t ON p.part_id = t.part_id AND t.ses_id = s.ses_id " +
                "WHERE p.act_codigo = :act_codigo " +
                "GROUP BY p.part_id " +
                "ORDER BY p.part_apellidos, p.part_nombre")
    fun getAllWithAsistencia(act_codigo: String, ses_id: Long): Flow<List<ParticipanteAsistencia>>

    /**
     * Devuelve el ID de un participante si se puede marcar asistencia
     */
    @Query("SELECT p.part_id AS id, p.part_nif AS nif, p.part_nombre AS nombre, p.part_apellidos AS apellidos, p.act_codigo AS actividad_codigo, " +
            "t.marca_temporal AS asistencia, t.tipo_registro AS tipo_registro, " +
            "s.ses_fin AS sesion_fin " +
            "FROM participantes p " +
            "INNER JOIN sesiones s ON s.act_codigo = p.act_codigo AND s.ses_id = :ses_id " +
            "LEFT JOIN asistencias t ON p.part_id = t.part_id AND t.ses_id = s.ses_id " +
            "WHERE p.act_codigo = :act_codigo AND p.part_nif = :part_nif AND s.ses_fin > :now")
    fun getWithAsistencia(act_codigo: String, part_nif: String, ses_id: Long, now: Date) : ParticipanteAsistencia
}