package es.murciaeduca.cprregionmurcia.registroasistencias.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import es.murciaeduca.cprregionmurcia.registroasistencias.database.entities.Sesion

@Dao
interface SesionDao {
    @Insert
    fun save(sesion: Sesion)
    @Update
    fun update(sesion: Sesion)
    @Delete
    fun delete(sesion: Sesion)
}