package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import androidx.room.*

@Entity(
    tableName = "participantes",
    primaryKeys = ["act_codigo", "part_nif"],
    indices = [
        Index(name="index_participantes_nombre_largo", value = ["part_nombre", "part_apellidos"]),
        Index("act_codigo"),
        Index("part_nif")
    ],
    foreignKeys = [
        ForeignKey(
            entity = ActividadEntity::class,
            parentColumns = ["act_codigo"],
            childColumns = ["act_codigo"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ParticipanteEntity(
    @ColumnInfo(name = "act_codigo")
    val actividad_codigo: String,
    @ColumnInfo(name = "part_nif")
    val nif: String,
    @ColumnInfo(name = "part_nombre")
    val nombre: String,
    @ColumnInfo(name = "part_apellidos")
    val apellidos: String,
    @ColumnInfo(name = "part_email")
    val email: String
) {
    override fun toString(): String {
        return "ParticipanteEntity(actividad_codigo='$actividad_codigo', nif='$nif', nombre='$nombre', apellidos='$apellidos', email='$email')"
    }
}
