package es.murciaeduca.cprregionmurcia.registroasistencias

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.ActivityMainBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.ui.auth.AuthViewModel
import es.murciaeduca.cprregionmurcia.registroasistencias.ui.auth.UserStatusMode


class MainActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Ocultar barra
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)

        // Vinculación de vistas
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    // Ir a home si el usuario está logueado y verificado
    override fun onStart() {
        super.onStart()
        if (viewModel.getUserStatus(auth) == UserStatusMode.VERIFIED) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}
