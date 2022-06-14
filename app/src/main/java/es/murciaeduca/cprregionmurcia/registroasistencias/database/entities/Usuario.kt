package es.murciaeduca.cprregionmurcia.registroasistencias.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey
    @ColumnInfo(name = "us_email")
    val email: String,
    @ColumnInfo(name = "us_nombre")
    val nombre: String,
    @ColumnInfo(name = "us_apellidos")
    val apellidos: String
){
    override fun toString(): String {
        return "Usuario(email='$email', nombre='$nombre', apellidos='$apellidos')\n"
    }
}

