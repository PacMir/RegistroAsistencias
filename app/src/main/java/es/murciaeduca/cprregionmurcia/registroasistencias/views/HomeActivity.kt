package es.murciaeduca.cprregionmurcia.registroasistencias.views

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Preferencias
        sp = getSharedPreferences("user",0)

        // Vinculación de vistas
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSetup()
    }

    // Mostrar datos del usuario
    private fun initSetup() {
        title = "Inicio"
        binding.emailView.text = sp.getString("userEmail", "asdf")
        binding.userView.text = sp.getString("userName", "")

        // Evento logout
        binding.logOutButton.setOnClickListener() {
            // Borrar datos de sesión
            with(sp.edit()) {
                clear()
                apply()
            }

            Firebase.auth.signOut()
            onBackPressed()
        }
    }
}
