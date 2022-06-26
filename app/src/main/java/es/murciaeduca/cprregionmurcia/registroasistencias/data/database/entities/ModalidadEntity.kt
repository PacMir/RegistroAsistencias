package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "modalidades")
data class ModalidadEntity(
    @PrimaryKey
    @ColumnInfo(name = "mod_codigo")
    val codigo: String,
    @ColumnInfo(name = "mod_denominacion")
    val denominacion: String,
) {
    override fun toString(): String {
        return "ModalidadEntity(codigo='$codigo', denominacion='$denominacion')"
    }
}
