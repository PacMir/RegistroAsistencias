package es.murciaeduca.cprregionmurcia.registroasistencias

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vinculación de vistas
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Recuperar los datos del la actividad de autenticación
        val bundle = intent.extras
        val email = bundle?.getString("email")
        setup(email ?: "")

        // Guardado de datos del usuario
        /*val user_session = getSharedPreferences(getString(R.string.user_session), Context.MODE_PRIVATE).edit()
        user_session.putString("email", email)
        user_session.apply()*/
    }

    // Mostrar datos del usuario
    private fun setup(email: String) {
        title = "Inicio"
        binding.emailView.text = email
        binding.userView.text = "Éxito"

        // Evento logout
        binding.logOutButton.setOnClickListener() {
            // Borrar datos de sesión
            /*val user_session = getSharedPreferences(getString(R.string.user_session), Context.MODE_PRIVATE).edit()
            user_session.clear()
            user_session.apply()*/

            Firebase.auth.signOut()
            onBackPressed()
        }
    }

    private fun checkCurrentUser() {
        // [START check_current_user]
        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }
        // [END check_current_user]
    }
}