package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import androidx.room.*
import java.util.*

@Entity(
    tableName = "sesiones",
    indices = [
        Index("ses_inicio"),
        Index("ses_fin"),
        Index("ses_carga_marca_temporal"),
        Index("act_codigo"),
        Index("us_email")
    ],
    foreignKeys = [
        ForeignKey(
            entity = ActividadEntity::class,
            parentColumns = ["act_codigo"],
            childColumns = ["act_codigo"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["us_email"],
            childColumns = ["us_email"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class SesionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ses_id")
    val id: Long = 0,
    @ColumnInfo(name = "ses_inicio")
    val inicio: Date,
    @ColumnInfo(name = "ses_fin")
    val fin: Date,
    @ColumnInfo(name = "ses_descarga_marca_temporal")
    val descarga_marca: String,
    @ColumnInfo(name = "ses_carga_marca_temporal")
    val carga_marca: String?,
    @ColumnInfo(name = "ses_observaciones")
    val observaciones: String?,
    @ColumnInfo(name = "act_codigo")
    val actividad_codigo: String,
    @ColumnInfo(name = "us_email")
    val usuario_email: String
) {
    override fun toString(): String {
        return "SesionEntity(id=$id, inicio='$inicio', fin='$fin', descarga_marca='$descarga_marca', carga_marca=$carga_marca, observaciones=$observaciones, actividad_codigo='$actividad_codigo', usuario_email='$usuario_email')\n"
    }
}

