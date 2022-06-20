package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "modalidades")
data class ModalidadEntity(
    @PrimaryKey
    @ColumnInfo(name = "mod_id")
    val id: Int,
    @ColumnInfo(name = "mod_denominacion")
    val denominacion: String
) {
    override fun toString(): String {
        return "ModalidadEntity(id=$id, modalidad='$denominacion')\n"
    }
}
