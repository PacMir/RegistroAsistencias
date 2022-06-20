package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.ParticipanteEntity

@Dao
interface ParticipanteDao {
    @Insert
    fun save(participante: ParticipanteEntity)

    @Update
    fun update(participante: ParticipanteEntity)

    @Delete
    fun delete(participante: ParticipanteEntity)

    @Query("SELECT * FROM participantes WHERE act_codigo = :codigo")
    fun getAllInActividad(codigo: String): List<ParticipanteEntity>

}