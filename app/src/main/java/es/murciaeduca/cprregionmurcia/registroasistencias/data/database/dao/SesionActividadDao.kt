package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.SesionActividad
import java.util.*

@Dao
interface SesionActividadDao {
    @Query("SELECT m.mod_denominacion AS modalidad, " +
            "a.act_codigo AS codigo, a.act_titulo AS titulo, a.nombre_responsable AS responsable, " +
            "s.ses_inicio AS inicio, s.ses_fin AS fin " +
            "FROM sesiones s " +
            "INNER JOIN actividades a ON s.act_codigo = a.act_codigo " +
            "INNER JOIN modalidades m ON a.mod_codigo = m.mod_codigo " +
            "WHERE a.user_email = :user_email AND s.ses_fin < :now")
    fun getPast(user_email: String, now: Date): List<SesionActividad>

    @Query("SELECT m.mod_denominacion AS modalidad, " +
            "a.act_codigo AS codigo, a.act_titulo AS titulo, a.nombre_responsable AS responsable, " +
            "s.ses_inicio AS inicio, s.ses_fin AS fin " +
            "FROM sesiones s " +
            "INNER JOIN actividades a ON s.act_codigo = a.act_codigo " +
            "INNER JOIN modalidades m ON a.mod_codigo = m.mod_codigo " +
            "WHERE a.user_email = :user_email AND s.ses_fin > :now")
    fun getToday(user_email: String, now: Date): List<SesionActividad>
}