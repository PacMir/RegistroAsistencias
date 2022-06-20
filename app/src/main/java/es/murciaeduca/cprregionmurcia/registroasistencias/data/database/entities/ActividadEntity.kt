package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import androidx.room.*

@Entity(
    tableName = "actividades",
    indices = [Index("mod_id")],
    foreignKeys = [
        ForeignKey(
            entity = ModalidadEntity::class,
            parentColumns = ["mod_id"],
            childColumns = ["mod_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
)
data class ActividadEntity(
    @PrimaryKey
    @ColumnInfo(name = "act_codigo")
    val codigo: String,
    @ColumnInfo(name = "act_titulo")
    val titulo: String,
    @ColumnInfo(name = "nombre_responsable")
    val responsable: String,
    @ColumnInfo(name = "email_responsable")
    val responsable_email: String,
    @ColumnInfo(name = "mod_id")
    val modalidad_id: Int
) {
    override fun toString(): String {
        return "ActividadEntity(codigo='$codigo', titulo='$titulo', responsable='$responsable', responsable_email='$responsable_email', modalidad_id=$modalidad_id)\n"
    }
}
