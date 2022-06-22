package es.murciaeduca.cprregionmurcia.registroasistencias.views

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.UsuarioDao
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.ActivityAuthBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AuthActivity : AppCompatActivity() {
    // private val auth: FirebaseAuth = Firebase.auth
      //private lateinit var sp: SharedPreferences
      private lateinit var binding: ActivityAuthBinding
      //private lateinit var userDao: UsuarioDao


      // Inicializar el estado del usuario como no logueado
     //private var userStatus = UserStatusMode.NOT_LOGGED


    override fun onCreate(savedInstanceState: Bundle?) {
        // Ocultar barra
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)

        // Vinculación de vistas
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
/*


        // UsuarioDao
        userDao = App.getInstance().usuarioDao()

        // Preferencias
        sp = getSharedPreferences("user", 0)

        // Configuración de listeners
        initSetup()*/
    }
/*
    override fun onStart() {
        super.onStart()

        // Obtener el estado del usuario y mostrar UI en función de este
        getUserStatus()
        updateUI()
    }

    // Configuración de listeners
    private fun initSetup() {
        val progressBar = binding.progressBar

        // Evento Registro
        binding.registerLink.setOnClickListener {

        }
        /*   binding.logInButton.setOnClickListener {
               if (binding.authEmail.text.isNotEmpty() && binding.authPassword.text.isNotEmpty()) {
                   progressBar.isVisible = true
                   createAccount()
               }
           }*/


    }

    // Actualizar la variable userStatus con el estado del usuario
    private fun getUserStatus() {
        val user = auth.currentUser
        userStatus = if (user == null) {
            UserStatusMode.NOT_LOGGED

        } else {
            user.reload()

            // Establecer en preferencias el nombre del usuario actual
            lifecycleScope.launch {
                val userName = withContext(Dispatchers.IO) { userDao.getLongName(user.email!!) }
                setSessionPreferences(userName, user.email!!)
            }

            if (user.isEmailVerified) UserStatusMode.VERIFIED else UserStatusMode.NOT_VERIFIED
        }
    }

    // Crear nueva cuenta
    private fun createAccount() {
        //val name = binding.userName.text.toString()
        //val lastName = binding.userLastName.text.toString()
        val email = binding.authEmail.text.toString()
        val password = binding.authPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Crear usuario
                    /*lifecycleScope.launch {
                        withContext(Dispatchers.IO){
                            userDao.save(UsuarioEntity(email, name, lastName))
                        }
                    }*/

                    // Enviar correo de verificación
                    sendEmailVerification()

                } else {
                    showAlert("Ha fallado la creación de la cuenta $it")
                }
            }
    }



    // Actualizar la vista en función del estado del usuario
    private fun updateUI() {

        // UsuarioEntity activo y email verificado, ocultar campos y redirección a Home
        when (userStatus) {
            UserStatusMode.VERIFIED -> {
                //  binding.authLayout.visibility = View.INVISIBLE
                goHome()
            }
            // UsuarioEntity activo y email no verificado, mostrar texto de espera de verificación y enlace de envío de correo (esperar 10min para que este esté activo)
            UserStatusMode.NOT_VERIFIED -> {
                showAlert("Han pasado " + getMinutesAfterEmailSent() + " minutos")

                // UsuarioEntity no autenticado
            }
            else -> {
                //  binding.authLayout.visibility = View.VISIBLE
            }
        }
    }



    // Navegar a la actividad Home
    private fun goHome() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    // Guardar datos del usuario en preferencias
    private fun setSessionPreferences(name: String, email: String) {
        with(sp.edit()) {
            putString("userName", name)
            putString("userEmail", email)
            apply()
        }
    }

    private companion object {
        // Número mínimo en minutos que deben pasar para reenviar un correo de verificación de usuario
        private const val MIN_MINUTES = 10
    }*/

}
