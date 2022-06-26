package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import java.util.*

@Entity(
    tableName = "asistencias",
    primaryKeys = ["act_codigo", "ses_inicio", "part_nif"],
    indices = [
        Index("act_codigo"),
        Index("ses_inicio"),
        Index("part_nif")
    ],
    foreignKeys = [
        ForeignKey(
            entity = ActividadEntity::class,
            parentColumns = ["act_codigo"],
            childColumns = ["act_codigo"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SesionEntity::class,
            parentColumns = ["act_codigo", "ses_inicio"],
            childColumns = ["act_codigo", "ses_inicio"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ParticipanteEntity::class,
            parentColumns = ["act_codigo", "part_nif"],
            childColumns = ["act_codigo", "part_nif"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AsistenciaEntity(
    @ColumnInfo(name = "act_codigo")
    val actividad_codigo: String,
    @ColumnInfo(name = "ses_inicio")
    val sesion_inicio: Date,
    @ColumnInfo(name = "part_nif")
    val participante_nif: String,
    @ColumnInfo(name = "factor")
    val factor: Float,
    @ColumnInfo(name = "marca_temporal")
    val marca_temporal: Date,
) {
    override fun toString(): String {
        return "AsistenciaEntity(actividad_codigo='$actividad_codigo', sesion_inicio=$sesion_inicio, participante_nif='$participante_nif', factor=$factor, marca_temporal='$marca_temporal')"
    }
}
