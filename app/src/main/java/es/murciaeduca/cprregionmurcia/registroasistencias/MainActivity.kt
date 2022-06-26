package es.murciaeduca.cprregionmurcia.registroasistencias

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.ActivityMainBinding

enum class UserStatusMode {
    NOT_LOGGED, NOT_VERIFIED, VERIFIED
}

class MainActivity : AppCompatActivity() {
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ocultar la barra superior
        supportActionBar?.hide()

        // Vinculación de vistas
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ir a home si el usuario está logueado y verificado
        if (getUserStatus() == UserStatusMode.VERIFIED) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()

        } else {
            auth.signOut()
        }
    }

    // Obtener el estado del usuario
    private fun getUserStatus(): UserStatusMode {
        val user = auth.currentUser

        val userStatus = if (user == null) {
            UserStatusMode.NOT_LOGGED

        } else {
            user.reload()
            if (user.isEmailVerified) UserStatusMode.VERIFIED else UserStatusMode.NOT_VERIFIED
        }

        return userStatus
    }
}
