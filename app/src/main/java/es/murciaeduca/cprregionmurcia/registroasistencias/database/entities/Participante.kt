package es.murciaeduca.cprregionmurcia.registroasistencias.database.entities

import androidx.room.*

@Entity(
    tableName = "participantes",
    indices = [
        Index(value = ["part_nif", "act_codigo"], unique = true),
        Index("part_nombre", "part_apellidos"),
        Index("act_codigo")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Actividad::class,
            parentColumns = ["act_codigo"],
            childColumns = ["act_codigo"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class Participante(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "part_id")
    val id: Long,
    @ColumnInfo(name = "part_nif")
    val nif: String,
    @ColumnInfo(name = "part_apellidos")
    val apellidos: String,
    @ColumnInfo(name = "part_nombre")
    val nombre: String,
    @ColumnInfo(name = "part_email")
    val email: String,
    @ColumnInfo(name = "act_codigo")
    val actividad_codigo: String
){
    override fun toString(): String {
        return "Participante(id=$id, nif='$nif', apellidos='$apellidos', nombre='$nombre', email='$email', actividad_codigo='$actividad_codigo')\n"
    }
}
