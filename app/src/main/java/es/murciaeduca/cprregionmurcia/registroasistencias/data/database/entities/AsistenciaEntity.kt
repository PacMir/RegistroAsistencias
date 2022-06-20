package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "asistencias",
    primaryKeys = ["part_id", "ses_id"],
    indices = [Index("ses_id")],
    foreignKeys = [
        ForeignKey(
            entity = ParticipanteEntity::class,
            parentColumns = ["part_id"],
            childColumns = ["part_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SesionEntity::class,
            parentColumns = ["ses_id"],
            childColumns = ["ses_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AsistenciaEntity(
    @ColumnInfo(name = "part_id")
    val participante_id: Long,
    @ColumnInfo(name = "ses_id")
    val sesion_id: Long,
    @ColumnInfo(name = "factor")
    val factor: Float,
    @ColumnInfo(name = "marca_temporal")
    val marca: String
) {
    override fun toString(): String {
        return "AsistenciaEntity(participante_id=$participante_id, sesion_id=$sesion_id, factor=$factor, marca='$marca')\n"
    }
}
