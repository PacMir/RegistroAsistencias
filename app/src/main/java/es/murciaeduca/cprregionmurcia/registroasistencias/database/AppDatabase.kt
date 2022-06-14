package es.murciaeduca.cprregionmurcia.registroasistencias.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import es.murciaeduca.cprregionmurcia.registroasistencias.database.entities.*
import es.murciaeduca.cprregionmurcia.registroasistencias.database.dao.*

@Database(
    entities = [
        Usuario::class,
        Sesion::class,
        Actividad::class,
        Modalidad::class,
        Participante::class,
        Asistencia::class
    ], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun sesionDao(): SesionDao
    abstract fun actividadDao(): ActividadDao
    abstract fun modalidadesDao(): ModalidadDao
    abstract fun ParticipanteDao(): ParticipanteDao
    abstract fun AsistenciaDao(): AsistenciaDao



    // Singleton
    companion object{
        private var db: AppDatabase? = null
        fun getDb(context: Context): AppDatabase{
            if(db == null) {
                db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java, "database"
                ).build()
            }
            return db!!
        }
    }
}