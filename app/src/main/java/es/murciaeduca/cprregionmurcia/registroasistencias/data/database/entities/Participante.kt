package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import androidx.annotation.NonNull
import androidx.room.*

@Entity(
    tableName = "participantes",
    indices = [
        Index(name = "index_participantes_candidata", value = ["act_codigo", "part_nif"], unique = true),
        Index(name="index_participantes_nombre_largo", value = ["part_nombre", "part_apellidos"]),
        Index("act_codigo"),
        Index("part_nif")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Actividad::class,
            parentColumns = ["act_codigo"],
            childColumns = ["act_codigo"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Participante(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "part_id")
    val id: Long,
    @NonNull @ColumnInfo(name = "act_codigo")
    val actividad_codigo: String,
    @NonNull @ColumnInfo(name = "part_nif")
    val nif: String,
    @NonNull @ColumnInfo(name = "part_nombre")
    val nombre: String,
    @NonNull @ColumnInfo(name = "part_apellidos")
    val apellidos: String,
    @NonNull @ColumnInfo(name = "part_email")
    val email: String
) {
    override fun toString(): String {
        return "Participante(id=$id, actividad_codigo='$actividad_codigo', nif='$nif', nombre='$nombre', apellidos='$apellidos', email='$email')"
    }
}
