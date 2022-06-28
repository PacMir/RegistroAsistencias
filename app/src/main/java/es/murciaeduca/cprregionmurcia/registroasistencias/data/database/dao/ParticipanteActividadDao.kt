package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.ParticipanteActividad
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ParticipanteActividadDao {
    @Query(
        "SELECT p.part_nif AS nif, p.part_nombre AS nombre, p.part_apellidos AS apellidos, p.act_codigo AS actividad_codigo, " +
                "t.marca_temporal AS asistencia, t.tipo_registro AS tipo_registro " +
                "FROM participantes p " +
                "LEFT JOIN asistencias t ON p.act_codigo = t.act_codigo AND p.part_nif = t.part_nif AND t.ses_inicio = :ses_inicio " +
                "WHERE p.act_codigo = :act_codigo " +
                "ORDER BY p.part_apellidos, p.part_nombre"
    )
    fun getAllInActividad(
        act_codigo: String,
        ses_inicio: Date,
    ): Flow<List<ParticipanteActividad>>
}
