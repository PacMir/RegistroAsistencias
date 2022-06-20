package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.UsuarioEntity

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun save(usuario: UsuarioEntity)

    @Query("SELECT us_nombre || ' ' || us_apellidos FROM usuarios WHERE us_email = :email LIMIT 1")
    suspend fun getLongName(email: String): String
}