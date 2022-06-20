package es.murciaeduca.cprregionmurcia.registroasistencias.application

import android.app.Application
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.AppDatabase

class App : Application() {
    companion object {
        private var db: AppDatabase? = null
        public fun getInstance(): AppDatabase {
            return db!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getInstance(applicationContext)
    }
}