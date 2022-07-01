package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.SesionActividad
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion
import kotlinx.coroutines.flow.Flow
import java.util.*

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
                "(SELECT COUNT(*) FROM participantes p WHERE p.act_codigo = a.act_codigo) AS num_participantes, " +
                "(SELECT COUNT(*) FROM asistencias st WHERE st.ses_id = s.ses_id) AS num_asisten " +
                "FROM sesiones s " +
                "INNER JOIN actividades a ON s.act_codigo = a.act_codigo " +
                "INNER JOIN modalidades m ON a.mod_codigo = m.mod_codigo " +
                "WHERE a.user_email = :user_email " +
                "AND s.ses_inicio BETWEEN :start_date AND :end_date " +
                "ORDER BY s.ses_inicio DESC"
    )
    fun getToday(user_email: String, start_date: Long, end_date: Long): Flow<List<SesionActividad>>

    @Transaction
    @Query("SELECT COUNT(*) " +
            "FROM sesiones s " +
            "INNER JOIN actividades a ON s.act_codigo = a.act_codigo " +
            "WHERE a.user_email = :user_email AND s.ses_inicio BETWEEN :start_date AND :end_date " +
            "ORDER BY s.ses_inicio ASC")
    fun getCountToday(user_email: String, start_date: Long, end_date: Long): LiveData<Int>

    /**
     *  Sesiones pasadas
     */
    @Transaction
    @Query(
        "SELECT s.ses_id AS id, s.ses_inicio AS inicio, s.ses_fin AS fin, s.ses_carga_marca_temporal AS upload, " +
                "a.act_codigo AS codigo, a.act_titulo AS titulo, a.nombre_responsable AS responsable, " +
                "m.mod_denominacion AS modalidad, " +
                "(SELECT COUNT(*) FROM participantes p WHERE p.act_codigo = a.act_codigo) AS num_participantes, " +
                "(SELECT COUNT(*) FROM asistencias st WHERE st.ses_id = s.ses_id) AS num_asisten " +
                "FROM sesiones s " +
                "INNER JOIN actividades a ON s.act_codigo = a.act_codigo " +
                "INNER JOIN modalidades m ON a.mod_codigo = m.mod_codigo " +
                "WHERE a.user_email = :user_email AND s.ses_fin < :now " +
                "ORDER BY s.ses_inicio ASC"
    )
    fun getPast(user_email: String, now: Date): Flow<List<SesionActividad>>

    @Transaction
    @Query("SELECT COUNT(*) " +
            "FROM sesiones s " +
            "INNER JOIN actividades a ON s.act_codigo = a.act_codigo " +
            "WHERE a.user_email = :user_email AND s.ses_fin < :now " +
            "ORDER BY s.ses_inicio ASC")
    fun getCountPast(user_email: String, now: Date): LiveData<Int>

    /**
     *  Sesiones pasadas pendientes de envío para el badge de notificaciones
     */
    @Query("SELECT COUNT(*) FROM sesiones s INNER JOIN actividades a ON s.act_codigo = a.act_codigo WHERE a.user_email = :user_email AND s.ses_fin < :now AND s.ses_carga_marca_temporal IS NULL")
    fun getNotSent(user_email: String, now: Date): LiveData<Int>

    /**
     * TODO A falta de implementar el envío de datos
     */
    @Query("UPDATE sesiones SET ses_carga_marca_temporal = :now WHERE ses_id = :sesion_id")
    suspend fun send(sesion_id: Long, now: Date)
}
