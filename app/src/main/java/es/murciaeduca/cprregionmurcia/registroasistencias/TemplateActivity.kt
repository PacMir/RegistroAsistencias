package es.murciaeduca.cprregionmurcia.registroasistencias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.database.entities.Actividad
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.ActivityTemplateBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TemplateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTemplateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vinculación de vistas
        binding = ActivityTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Corrutina de entrada y salida de datos
        lifecycleScope.launch {
            val actividades: List<Actividad> = withContext(Dispatchers.IO) {
                App.getDb().actividadDao().getAll()
            }
        }

        // Recupera los datos del usuario de SharedPreferences y oculta el layout de autenticación si existen
        /*private fun session() {
            val user = auth.currentUser
            user?.let {
                val email = user.email
                val verified = user.isEmailVerified

                showAlert(name + email + verified)
            }*/
        /*
        val user_session = getSharedPreferences(getString(R.string.user_session), Context.MODE_PRIVATE)
        val email = user_session.getString("email", null)
        if(email != null) {
            binding.authLayout.visibility = View.INVISIBLE
            goHome(email)
        }

    }*/
    }
}