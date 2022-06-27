package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.Participante

@Dao
interface ParticipanteDao {
    @Insert
    fun save(participante: Participante)

    @Update
    fun update(participante: Participante)

    @Delete
    fun delete(participante: Participante)

    @Query("SELECT * FROM participantes WHERE act_codigo = :codigo")
    fun getAllInActividad(codigo: String): List<Participante>

}