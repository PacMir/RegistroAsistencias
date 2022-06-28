package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Sesion

@Dao
interface SesionDao {
    @Insert
    fun save(sesion: Sesion)

    @Update
    fun update(sesion: Sesion)

    @Delete
    fun delete(sesion: Sesion)
}