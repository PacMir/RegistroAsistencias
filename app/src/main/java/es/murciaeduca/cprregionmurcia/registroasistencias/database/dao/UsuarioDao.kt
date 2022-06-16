package es.murciaeduca.cprregionmurcia.registroasistencias.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import es.murciaeduca.cprregionmurcia.registroasistencias.database.entities.Usuario

@Dao
interface UsuarioDao {
    @Insert
    fun save(usuario: Usuario)
    @Update
    fun update(usuario: Usuario)

    @Query("SELECT * FROM usuario WHERE Email = :email")
    fun getUsuarioByEmail(email: String): Usuario
}