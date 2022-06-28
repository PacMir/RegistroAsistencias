package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "modalidades")
data class Modalidad(
    @PrimaryKey
    @NonNull @ColumnInfo(name = "mod_codigo")
    val codigo: String,
    @NonNull @ColumnInfo(name = "mod_denominacion")
    val denominacion: String,
) {
    override fun toString(): String {
        return "Modalidad(codigo='$codigo', denominacion='$denominacion')"
    }
}
