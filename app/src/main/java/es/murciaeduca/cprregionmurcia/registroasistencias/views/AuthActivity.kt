package es.murciaeduca.cprregionmurcia.registroasistencias

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.dao.UsuarioDao
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.UsuarioEntity
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.ActivityAuthBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.views.HomeActivity
import kotlinx.coroutines.launch
import java.util.*

enum class UserStatusMode {
    NOT_LOGGED, NOT_VERIFIED, VERIFIED
}

class AuthActivity : AppCompatActivity() {
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var sp: SharedPreferences
    private lateinit var binding: ActivityAuthBinding

    // Vista-Modelo usuario
    //private lateinit var viewModel: UsuarioViewModel
    private lateinit var userDao : UsuarioDao

    // Inicializar el estado del usuario como no logueado
    private var userStatus = UserStatusMode.NOT_LOGGED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Viewmodel
      /*  val repository = UsuarioRepository()
        val vm : UsuarioViewModel by viewModels {
            UsuarioViewModel.Factory(repository)
        }
        viewModel = vm*/
        userDao = App.getInstance().usuarioDao()

        // Preferencias
        sp = getPreferences(0)

        // Vinculación de vistas
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración de listeners
        initSetup()
    }

    override fun onStart() {
        super.onStart()

        // Obtener el estado del usuario y mostrar UI en función de este
        getUserStatus()
        updateUI()
    }

    // Configuración de listeners
    private fun initSetup() {
        title = "Autenticación"
        val progressBar = binding.progressBar

        // Evento Registro
        binding.signUpButton.setOnClickListener {
            if (binding.authEmail.text.isNotEmpty() && binding.authPassword.text.isNotEmpty()) {
                progressBar.isVisible = true
                createAccount()
            }
        }

        // Evento Inicio sesión
        binding.loginButton.setOnClickListener {
            if (binding.authEmail.text.isNotEmpty() && binding.authPassword.text.isNotEmpty()) {
                progressBar.isVisible = true
                logIn()
            }
        }
    }

    // Actualizar la variable userStatus con el estado del usuario
    private fun getUserStatus() {
        val user = auth.currentUser
        if (user == null) {
            userStatus = UserStatusMode.NOT_LOGGED

        } else {
            user.reload()

            // Establecer en preferencias el nombre del usuario actual
            //val userName = viewModel.getLongName(user.email!!)
            lifecycleScope.launch {
                val userName = userDao.getLongName(user.email!!)
                sp.edit().putString("userName", userName)
                sp.edit().apply()
            }

            userStatus =
                if (user.isEmailVerified) UserStatusMode.VERIFIED else UserStatusMode.NOT_VERIFIED
        }
    }

    // Crear nueva cuenta
    private fun createAccount() {
        val name = binding.userName.text.toString()
        val lastName = binding.userLastName.text.toString()
        val email = binding.authEmail.text.toString()
        val password = binding.authPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Crear usuario
                    //viewModel.save(UsuarioEntity(email, name, lastName))
                    lifecycleScope.launch {
                        userDao.save(UsuarioEntity(email, name, lastName))
                    }
                    sp.edit().putString("userName", "$name $lastName")
                    sp.edit().apply()

                    // Enviar correo de verificación
                    sendEmailVerification()

                } else {
                    showAlert("Ha fallado la creación de la cuenta $it")
                }
            }
    }

    // Iniciar sesión
    private fun logIn() {
        val email = binding.authEmail.text.toString()
        val password = binding.authPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showAlert(sp.getString("userName", "").toString())
                    updateUI()
                } else {
                    showAlert("Ha fallado la autenticación")
                }
            }
    }

    // Enviar/reenviar email de verificación
    private fun sendEmailVerification() =
        // Comprobar que han pasado más de 10 minutos desde el último correo
        if (userStatus == UserStatusMode.NOT_VERIFIED && getMinutesAfterEmailSent() < 10) {
            showAlert("Por favor, espere $MIN_MINUTES minutos para volver a solicitar otro correo de confirmación.")

        } else {

            val user = auth.currentUser!!
            user.sendEmailVerification()
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        // Guardar la fecha y hora a la que se envió el correo, en milisegundos, en las preferencias de la actividad
                        with(sp.edit()) {
                            putString("userEmail", user.email)
                            putLong("lastEmailSent", Calendar.getInstance().timeInMillis)
                            apply()
                        }
                    }
                }
                .addOnFailureListener {
                    showAlert("No se ha podido enviar el correo de verificación. Por favor, inténtelo de nuevo pasados unos minutos.")
                }

            // Actualizar la vista
            updateUI()
        }

    // Calcular el tiempo que ha pasado desde que se envió el email
    private fun getMinutesAfterEmailSent(): Long {
        val now = Calendar.getInstance().timeInMillis
        val lastEmailSent = sp.getLong("lastEmailSent", now)

        return (now - lastEmailSent) / 60000
    }

    // Actualizar la vista en función del estado del usuario
    private fun updateUI() {

        // UsuarioEntity activo y email verificado, ocultar campos y redirección a Home
        when (userStatus) {
            UserStatusMode.VERIFIED -> {
                binding.authLayout.visibility = View.INVISIBLE
                goHome()
            }
            // UsuarioEntity activo y email no verificado, mostrar texto de espera de verificación y enlace de envío de correo (esperar 10min para que este esté activo)
            UserStatusMode.NOT_VERIFIED -> {
                showAlert("Han pasado " + getMinutesAfterEmailSent() + " minutos")

                // UsuarioEntity no autenticado
            }
            else -> {
                binding.authLayout.visibility = View.VISIBLE
            }
        }
    }

    // Mostrar alertas
    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Atención")
        builder.setMessage(message)
        builder.setPositiveButton("Cerrar", null)
        builder.show()
    }

    // Navegar a la actividad Home
    private fun goHome() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    companion object {
        // Número mínimo en minutos que deben pasar para reenviar un correo de verificación de usuario
        private const val MIN_MINUTES = 10
    }

}