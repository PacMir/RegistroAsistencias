package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey
    @ColumnInfo(name = "us_email")
    val email: String,
    @ColumnInfo(name = "us_nombre")
    val nombre: String,
    @ColumnInfo(name = "us_apellidos")
    val apellidos: String
) {
    override fun toString(): String {
        return "UsuarioEntity(email='$email', nombre='$nombre', apellidos='$apellidos')\n"
    }
}
