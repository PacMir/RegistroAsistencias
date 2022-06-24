package es.murciaeduca.cprregionmurcia.registroasistencias.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.*
import es.murciaeduca.cprregionmurcia.registroasistencias.utils.DateConverter

@Database(
    entities = [UsuarioEntity::class, SesionEntity::class, ActividadEntity::class, ModalidadEntity::class, ParticipanteEntity::class, AsistenciaEntity::class],
    exportSchema = false,
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun sesionDao(): SesionDao
    abstract fun actividadDao(): ActividadDao
    abstract fun modalidadesDao(): ModalidadDao
    abstract fun participanteDao(): ParticipanteDao
    abstract fun asistenciaDao(): AsistenciaDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton de instancia de conexión a la base de datos
        fun getInstance(context: Context): AppDatabase {

            // Eliminar base de datos
            //context.deleteDatabase(DATABASE_NAME);

            // Crear instancia database si no es null
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    // .addCallback(ModalidadesPopulator())
                    .build()
                INSTANCE = instance

                instance
            }
        }

        // Prepopagar la base de datos
        /*private class ModalidadesPopulator() : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                GlobalScope.launch {
                    INSTANCE?.let { db ->
                        GlobalScope.launch {
                            val dao = db.modalidadesDao()
                            dao.populateData(PREP_MODS)
                        }
                    }
                }
            }
        }*/

        // Datos iniciales de modalidades
        private val PREP_MODS = listOf(
            ModalidadEntity(1, "Congreso"),
            ModalidadEntity(2, "Curso"),
            ModalidadEntity(3, "Grupo de trabajo"),
            ModalidadEntity(4, "Jornadas"),
            ModalidadEntity(5, "Proyecto de formación en centros"),
            ModalidadEntity(6, "Seminario temático"),
            ModalidadEntity(7, "Seminario de equipo docente"),
            ModalidadEntity(8, "Proyecto de innovación"),
            ModalidadEntity(9, "Proyecto de investigación")
        )

        // Nombre de la base de datos
        private val DATABASE_NAME = "database"
    }
}
