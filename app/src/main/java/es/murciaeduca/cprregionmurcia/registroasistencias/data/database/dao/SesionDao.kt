package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion
import java.util.*

@Dao
interface SesionDao {
    @Query("SELECT * FROM sesiones WHERE ses_fin < :now")
    fun getPast(now: Date): List<Sesion>

    @Query("SELECT * FROM sesiones WHERE ses_fin > :now")
    fun getToday(now: Date): List<Sesion>

    @Insert
    fun save(sesion: Sesion)

    @Update
    fun update(sesion: Sesion)

    @Delete
    fun delete(sesion: Sesion)
}