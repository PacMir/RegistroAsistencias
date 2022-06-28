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
            entity = Actividad::class,
            parentColumns = ["act_codigo"],
            childColumns = ["act_codigo"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Sesion::class,
            parentColumns = ["act_codigo", "ses_inicio"],
            childColumns = ["act_codigo", "ses_inicio"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Participante::class,
            parentColumns = ["act_codigo", "part_nif"],
            childColumns = ["act_codigo", "part_nif"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Asistencia(
    @ColumnInfo(name = "act_codigo")
    val actividad_codigo: String,
    @ColumnInfo(name = "ses_inicio")
    val sesion_inicio: Date,
    @ColumnInfo(name = "part_nif")
    val participante_nif: String,
    @ColumnInfo(name = "factor")
    val factor: Float,
    @ColumnInfo(name= "tipo_registro")
    val tipo_registro: Int,
    @ColumnInfo(name = "marca_temporal")
    val marca_temporal: Date,
) {
    override fun toString(): String {
        return "Asistencia(actividad_codigo='$actividad_codigo', sesion_inicio=$sesion_inicio, participante_nif='$participante_nif', factor=$factor, tipo_registro=$tipo_registro, marca_temporal=$marca_temporal)"
    }
}
