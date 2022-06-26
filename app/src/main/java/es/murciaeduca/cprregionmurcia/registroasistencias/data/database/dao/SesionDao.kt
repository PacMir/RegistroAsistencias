package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.SesionEntity
import java.util.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SesionDao {
    @Query("SELECT * FROM sesiones WHERE ses_fin < :now")
    fun getPast(now: Date): Flow<List<SesionEntity>>

    @Query("SELECT * FROM sesiones WHERE ses_fin > :now")
    fun getToday(now: Date): Flow<List<SesionEntity>>

    @Insert
    fun save(sesion: SesionEntity)

    @Update
    fun update(sesion: SesionEntity)

    @Delete
    fun delete(sesion: SesionEntity)
}