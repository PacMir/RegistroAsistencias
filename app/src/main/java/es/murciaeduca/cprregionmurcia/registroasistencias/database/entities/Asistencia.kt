package es.murciaeduca.cprregionmurcia.registroasistencias.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "asistencias",
    primaryKeys = ["part_id", "ses_id"],
    foreignKeys = [
        ForeignKey(
            entity = Participante::class,
            parentColumns = ["part_id"],
            childColumns = ["part_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Sesion::class,
            parentColumns = ["ses_id"],
            childColumns = ["ses_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Asistencia(
    @ColumnInfo(name = "part_id")
    val participante_id: Long,
    @ColumnInfo(name = "ses_id")
    val sesion_id: Long,
    @ColumnInfo(name = "factor")
    val factor: Float,
    @ColumnInfo(name = "marca_temporal")
    val marca: String
){
    override fun toString(): String {
        return "Asistencia(participante_id=$participante_id, sesion_id=$sesion_id, factor=$factor, marca='$marca')\n"
    }
}
