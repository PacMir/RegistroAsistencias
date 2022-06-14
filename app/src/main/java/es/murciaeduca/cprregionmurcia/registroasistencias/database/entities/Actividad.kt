package es.murciaeduca.cprregionmurcia.registroasistencias.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "actividades",
    foreignKeys = [
        ForeignKey(
            entity = Modalidad::class,
            parentColumns = ["mod_id"],
            childColumns = ["mod_id"],
            onDelete = ForeignKey.RESTRICT)
    ],
)
data class Actividad(
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
){
    override fun toString(): String {
        return "Actividad(codigo='$codigo', titulo='$titulo', responsable='$responsable', responsable_email='$responsable_email', modalidad_id=$modalidad_id)\n"
    }
}
