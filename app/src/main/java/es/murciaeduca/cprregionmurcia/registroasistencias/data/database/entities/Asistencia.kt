package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

@Entity(
    tableName = "asistencias",
    indices = [
        Index(name = "index_asistencias_candidata", value = ["ses_id", "part_id"], unique = true),
        Index("ses_id"),
        Index("part_id"),
        Index("tipo_registro")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Sesion::class,
            parentColumns = ["ses_id"],
            childColumns = ["ses_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Participante::class,
            parentColumns = ["part_id"],
            childColumns = ["part_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Asistencia(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @NonNull @ColumnInfo(name = "ses_id")
    val sesion_id: Long,
    @NonNull @ColumnInfo(name = "part_id")
    val participante_id: Long,
    @NonNull
    val factor: Float,
    @NonNull
    val tipo_registro: Int,
    @NonNull
    val marca_temporal: Date,
) {
    override fun toString(): String {
        return "Asistencia(id=$id, sesion_id=$sesion_id, participante_id=$participante_id, factor=$factor, tipo_registro=$tipo_registro, marca_temporal=$marca_temporal)"
    }
}
