package es.murciaeduca.cprregionmurcia.registroasistencias.application

import android.app.Application
import es.murciaeduca.cprregionmurcia.registroasistencias.database.AppDatabase

class App: Application() {

    // Se pasa siempre un contexto a la conexión a la base de datos
    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getDb(applicationContext)
    }

    companion object {
        private var db: AppDatabase? = null
        public fun getDb(): AppDatabase {
            return db!!
        }
    }
}