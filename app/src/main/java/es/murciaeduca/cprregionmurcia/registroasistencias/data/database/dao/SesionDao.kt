package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.SesionEntity

@Dao
interface SesionDao {
    @Query("SELECT * FROM sesiones")
    fun getAll(): LiveData<List<SesionEntity>>

    @Insert
    fun save(sesion: SesionEntity)

    @Update
    fun update(sesion: SesionEntity)

    @Delete
    fun delete(sesion: SesionEntity)
}