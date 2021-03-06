package es.murciaeduca.cprregionmurcia.registroasistencias.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.*
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.*
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateConverter

@Database(
    entities = [Sesion::class, Actividad::class, Modalidad::class, Participante::class, Asistencia::class],
    exportSchema = true,
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sesionDao(): SesionDao
    //abstract fun actividadDao(): ActividadDao
    //abstract fun modalidadesDao(): ModalidadDao
    abstract fun participanteDao(): ParticipanteDao
    abstract fun asistenciaDao(): AsistenciaDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton de instancia de conexión a la base de datos
        fun getInstance(context: Context): AppDatabase {

            // Eliminar base de datos
            //context.deleteDatabase(DATABASE_NAME)

            // Crear instancia database si no es null
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    // Prepropagar con Base de datos
                    .createFromAsset("database/$DATABASE_NAME.db")
                    // No migraciones
                    //.fallbackToDestructiveMigration()
                    // Prepropagar modalidades
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
        }

        // Datos iniciales de modalidades
        private val PREP_MODS = listOf(
            Modalidad("CON", "Congreso"),
            Modalidad("CUR", "Curso"),
            Modalidad("GT", "Grupo de trabajo"),
            Modalidad("JOR", "Jornadas"),
            Modalidad("PFC", "Proyecto de formación en centros"),
            Modalidad("ST", "Seminario temático"),
            Modalidad("SED", "Seminario de equipo docente"),
            Modalidad("PIE", "Proyecto de innovación"),
            Modalidad("PIV", "Proyecto de investigación")
        )
         */

        // Nombre de la base de datos
        private const val DATABASE_NAME = "database"
    }
}
