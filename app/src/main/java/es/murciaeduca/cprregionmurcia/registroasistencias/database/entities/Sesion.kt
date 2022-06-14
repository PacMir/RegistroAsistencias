package es.murciaeduca.cprregionmurcia.registroasistencias.database.entities

import androidx.room.*

@Entity(
    tableName = "sesiones",
    indices = [
        Index("ses_inicio"),
        Index("ses_fin"),
        Index("ses_carga_marca_temporal"),
        Index("act_codigo")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Actividad::class,
            parentColumns = ["act_codigo"],
            childColumns = ["act_codigo"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["us_email"],
            childColumns = ["us_email"],
            onDelete = ForeignKey.CASCADE)
    ],
)
data class Sesion(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ses_id")
    val id: Long,
    @ColumnInfo(name = "ses_inicio")
    val inicio: String,
    @ColumnInfo(name = "ses_fin")
    val fin: String,
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
){
    override fun toString(): String {
        return "Sesion(id=$id, inicio='$inicio', fin='$fin', descarga_marca='$descarga_marca', carga_marca=$carga_marca, observaciones=$observaciones, actividad_codigo='$actividad_codigo', usuario_email='$usuario_email')\n"
    }
}

