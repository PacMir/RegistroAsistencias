package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.SesionActividad
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion
import kotlinx.coroutines.flow.Flow

@Dao
interface SesionDao {
    @Insert
    suspend fun save(sesion: Sesion)

    @Update
    suspend fun update(sesion: Sesion)

    @Delete
    suspend fun delete(sesion: Sesion)

    @Query("SELECT * FROM sesiones WHERE ses_id = :sesion_id")
    suspend fun getById(sesion_id: Long): Sesion

    /**
     * Búsqueda de sesiones en el día de hoy
     */
    @Transaction
    @Query(
        "SELECT s.ses_id AS id, s.ses_inicio AS inicio, s.ses_fin AS fin, " +
                "a.act_codigo AS codigo, a.act_titulo AS titulo, a.nombre_responsable AS responsable, " +
                "m.mod_denominacion AS modalidad, " +
                "CASE WHEN strftime('%s', 'now') * 1000 BETWEEN s.ses_inicio AND s.ses_fin THEN 1 ELSE 0 END AS activa, " +
                "(SELECT COUNT(*) FROM participantes p WHERE p.act_codigo = a.act_codigo) AS num_participantes, " +
                "(SELECT COUNT(*) FROM asistencias st WHERE st.ses_id = s.ses_id) AS num_asisten " +
                "FROM sesiones s " +
                "INNER JOIN actividades a ON s.act_codigo = a.act_codigo " +
                "INNER JOIN modalidades m ON a.mod_codigo = m.mod_codigo " +
                "WHERE a.user_email = :user_email " +
                "AND s.ses_inicio BETWEEN strftime('%s', 'now', 'start of day') * 1000 " +
                "AND strftime('%s', 'now', 'start of day', '+1 day') * 1000 " +
                "ORDER BY s.ses_inicio DESC"
    )
    fun getToday(user_email: String): Flow<List<SesionActividad>>

    @Transaction
    @Query("SELECT COUNT(*) " +
            "FROM sesiones s " +
            "INNER JOIN actividades a ON s.act_codigo = a.act_codigo " +
            "WHERE a.user_email = :user_email AND s.ses_inicio BETWEEN strftime('%s', 'now', 'start of day') * 1000 " +
            "AND strftime('%s', 'now', 'start of day', '+1 day') * 1000")
    fun getCountToday(user_email: String): LiveData<Int>

    /**
     *  Sesiones pasadas
     */
    @Transaction
    @Query(
        "SELECT s.ses_id AS id, s.ses_inicio AS inicio, s.ses_fin AS fin, s.ses_carga_marca_temporal AS upload, " +
                "a.act_codigo AS codigo, a.act_titulo AS titulo, a.nombre_responsable AS responsable, " +
                "m.mod_denominacion AS modalidad, " +
                "0 AS activa, " +
                "(SELECT COUNT(*) FROM participantes p WHERE p.act_codigo = a.act_codigo) AS num_participantes, " +
                "(SELECT COUNT(*) FROM asistencias st WHERE st.ses_id = s.ses_id) AS num_asisten " +
                "FROM sesiones s " +
                "INNER JOIN actividades a ON s.act_codigo = a.act_codigo " +
                "INNER JOIN modalidades m ON a.mod_codigo = m.mod_codigo " +
                "WHERE a.user_email = :user_email AND s.ses_fin < strftime('%s', 'now') * 1000 " +
                "ORDER BY s.ses_inicio ASC"
    )
    fun getPast(user_email: String): Flow<List<SesionActividad>>

    @Transaction
    @Query("SELECT COUNT(*) " +
            "FROM sesiones s " +
            "INNER JOIN actividades a ON s.act_codigo = a.act_codigo " +
            "WHERE a.user_email = :user_email AND s.ses_fin < strftime('%s', 'now') * 1000")
    fun getCountPast(user_email: String): LiveData<Int>

    /**
     *  Sesiones pasadas pendientes de envío para el badge de notificaciones
     */
    @Query("SELECT COUNT(*) FROM sesiones s INNER JOIN actividades a ON s.act_codigo = a.act_codigo " +
            "WHERE a.user_email = :user_email AND s.ses_fin < strftime('%s', 'now') * 1000 AND s.ses_carga_marca_temporal IS NULL")
    fun getNotSent(user_email: String): LiveData<Int>

    /**
     * TODO A falta de implementar el envío de datos
     */
    @Query("UPDATE sesiones SET ses_carga_marca_temporal = strftime('%s', 'now') * 1000 WHERE ses_id = :sesion_id")
    suspend fun send(sesion_id: Long)
}
