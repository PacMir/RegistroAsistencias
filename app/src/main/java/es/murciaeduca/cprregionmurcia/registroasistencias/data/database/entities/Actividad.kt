package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import androidx.annotation.NonNull
import androidx.room.*

@Entity(
    tableName = "actividades",
    indices = [
        Index("user_email"),
        Index("mod_codigo")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Modalidad::class,
            parentColumns = ["mod_codigo"],
            childColumns = ["mod_codigo"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
)
data class Actividad(
    @PrimaryKey
    @NonNull @ColumnInfo(name = "act_codigo")
    val codigo: String,
    @NonNull @ColumnInfo(name = "act_titulo")
    val titulo: String,
    @NonNull @ColumnInfo(name = "user_email")
    val usuario_email: String,
    @NonNull @ColumnInfo(name = "mod_codigo")
    val modalidad_codigo: String,
    @NonNull @ColumnInfo(name = "nombre_responsable")
    val responsable: String,
    @NonNull @ColumnInfo(name = "email_responsable")
    val responsable_email: String,
) {
    override fun toString(): String {
        return "Actividad(codigo='$codigo', titulo='$titulo', usuario_email='$usuario_email', modalidad_codigo='$modalidad_codigo', responsable='$responsable', responsable_email='$responsable_email')"
    }
}
