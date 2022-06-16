package es.murciaeduca.cprregionmurcia.registroasistencias

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.database.entities.Usuario
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.ActivityAuthBinding
import java.util.*
import java.util.regex.Pattern

enum class UserStatusMode {
    NOT_LOGGED, NOT_VERIFIED, VERIFIED
}

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sp: SharedPreferences
    private lateinit var userDb: Usuario

    // Inicializar el estaod del usuario como no logueado
    private var userStatus = UserStatusMode.NOT_LOGGED
    // Número mínimo en minutos que deben de pasar para reenviar un correo de verificación de usuario
    private val min_minutes = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vinculación de vistas
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener preferencias de la actividad
        sp = getPreferences(0)

        // Inicializar Firebase
        auth = Firebase.auth

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

        // Evento Registro
        binding.signUpButton.setOnClickListener {
            if (binding.authEmail.text.isNotEmpty() && binding.authPassword.text.isNotEmpty()){
               createAccount()
            }
        }

        // Evento Inicio sesión
        binding.loginButton.setOnClickListener() {
            if (binding.authEmail.text.isNotEmpty() && binding.authPassword.text.isNotEmpty()){
                logIn()
            }
        }
    }

    // Actualizar la variable userStatus con el estado del usuario
    private fun getUserStatus() {
        val user = auth.currentUser
        if(user == null) {
            userStatus = UserStatusMode.NOT_LOGGED
        } else {
            user.reload()
            // userDb = TODO() obtener datos del usuario (ver uso de room)
            userStatus = if(user.isEmailVerified) UserStatusMode.VERIFIED else UserStatusMode.NOT_VERIFIED
        }
    }

    // Crear nueva cuenta
    private fun createAccount() {
        val email = binding.authEmail.text.toString()
        val password = binding.authPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Crear usuario
                    userDb = Usuario(email, "Nombre", "Apellidos")

                    // Enviar correo de verificación
                    sendEmailVerification()

                } else {
                    showAlert("Ha fallado la creación de la cuenta " + it.toString())
                }
            }
    }

    // Iniciar sesión
    private fun logIn() {
        val email = binding.authEmail.text.toString()
        val password = binding.authPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    updateUI()
                } else {
                    showAlert("Ha fallado la autenticación")
                }
            }
    }

    // Enviar/reenviar email de verificación
    private fun sendEmailVerification() {
        // Comprobar que han pasado más de 10 minutos desde el último correo
        if(userStatus == UserStatusMode.NOT_VERIFIED && getMinutesAfterEmailSent() < 10){
            showAlert("Por favor, espere " + min_minutes + " minutos para volver a solicitar otro correo de confirmación.")

        }else {

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
                .addOnFailureListener { e ->
                    showAlert("No se ha podido enviar el correo de verificación. Por favor, inténtelo de nuevo pasados unos minutos.")
                }

            // Actualizar la vista
            updateUI()
        }
    }

    // Calcular el tiempo que ha pasado desde que se envió el email
    private fun getMinutesAfterEmailSent(): Long {
        val now = Calendar.getInstance().timeInMillis
        val lastEmailSent = sp.getLong("lastEmailSent", now)

        return (now - lastEmailSent) / 60000
    }

    // Validar un email
    private fun isValidEmail(str: String): Boolean{
        val email_pattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return email_pattern.matcher(str).matches()
    }

    // Actualizar la vista en función del estado del usuario
    private fun updateUI() {

        // Usuario activo y email verificado, ocultar campos y redirección a Home
        when(userStatus) {
            UserStatusMode.VERIFIED -> {
                binding.authLayout.visibility = View.INVISIBLE
                goHome()
            }
                // Usuario activo y email no verificado, mostrar texto de espera de verificación y enlace de envío de correo (esperar 10min para que este esté activo)
            UserStatusMode.NOT_VERIFIED -> {
                showAlert("Han pasado " + getMinutesAfterEmailSent() + " minutos")

                // Usuario no autenticado
            } else -> {
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

}

