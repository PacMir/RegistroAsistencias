package es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities

import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

@Entity(
    tableName = "sesiones",
    indices = [
        Index(name = "index_sesiones_candidata", value = ["act_codigo", "ses_inicio"], unique = true),
        Index("act_codigo"),
        Index("ses_inicio"),
        Index("ses_fin"),
        Index("ses_carga_marca_temporal")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Actividad::class,
            parentColumns = ["act_codigo"],
            childColumns = ["act_codigo"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)
data class Sesion(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ses_id")
    val id: Long,
    @NonNull @ColumnInfo(name = "act_codigo")
    val actividad_codigo: String,
    @NonNull @ColumnInfo(name = "ses_inicio")
    val inicio: Date,
    @NonNull @ColumnInfo(name = "ses_fin")
    val fin: Date,
    @ColumnInfo(name = "ses_carga_marca_temporal")
    val carga_marca_temporal: Date?,
    @ColumnInfo(name = "ses_observaciones")
    val observaciones: String?,
) {
    override fun toString(): String {
        return "Sesion(id=$id, actividad_codigo='$actividad_codigo', inicio=$inicio, fin=$fin, carga_marca_temporal=$carga_marca_temporal, observaciones=$observaciones)"
    }
}

