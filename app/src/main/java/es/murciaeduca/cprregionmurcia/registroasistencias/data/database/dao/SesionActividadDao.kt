package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.SesionActividad
import java.util.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SesionActividadDao {
    @Query(
        "SELECT m.mod_denominacion AS modalidad, " +
                "a.act_codigo AS codigo, a.act_titulo AS titulo, a.nombre_responsable AS responsable, " +
                "s.ses_inicio AS inicio, s.ses_fin AS fin, " +
                "(SELECT COUNT(*) FROM participantes p WHERE p.act_codigo = a.act_codigo) AS num_participantes " +
                "FROM sesiones s " +
                "INNER JOIN actividades a ON s.act_codigo = a.act_codigo " +
                "INNER JOIN modalidades m ON a.mod_codigo = m.mod_codigo " +
                "WHERE a.user_email = :user_email AND s.ses_fin < :now " +
                "ORDER BY s.ses_inicio DESC"
    )
    fun getPast(user_email: String, now: Date): Flow<List<SesionActividad>>

    @Query(
        "SELECT m.mod_denominacion AS modalidad, " +
                "a.act_codigo AS codigo, a.act_titulo AS titulo, a.nombre_responsable AS responsable, " +
                "s.ses_inicio AS inicio, s.ses_fin AS fin, " +
                "(SELECT COUNT(*) FROM participantes p WHERE p.act_codigo = a.act_codigo) AS num_participantes " +
                "FROM sesiones s " +
                "INNER JOIN actividades a ON s.act_codigo = a.act_codigo " +
                "INNER JOIN modalidades m ON a.mod_codigo = m.mod_codigo " +
                "WHERE a.user_email = :user_email AND s.ses_fin > :now " +
                "ORDER BY s.ses_inicio ASC"
    )
    fun getToday(user_email: String, now: Date): Flow<List<SesionActividad>>
}