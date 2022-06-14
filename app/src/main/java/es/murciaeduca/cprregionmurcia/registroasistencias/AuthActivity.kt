package es.murciaeduca.cprregionmurcia.registroasistencias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.ActivityAuthBinding

enum class UserStatusMode {
    NOT_LOGGED, LOGGED, NOT_VERIFIED, VERIFIED
}

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var auth: FirebaseAuth
    private var userStatus = UserStatusMode.NOT_LOGGED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vinculación de vistas
        binding = ActivityAuthBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Inicializar Firebase
        auth = Firebase.auth

        // Función de configuración
        setup()

    }

    override fun onStart() {
        super.onStart()

        binding.authLayout.visibility = View.VISIBLE

        // Obtener el estado del usuario
        getUserStatus()
    }

    // Configuración de listeners
    private fun setup() {
        title = "Autenticación"

        // Evento registrarse
        binding.signUpButton.setOnClickListener {
            if (binding.authEmail.text.isNotEmpty() && binding.authPassword.text.isNotEmpty()){
               createAccount()
            }
        }

        // Evento loguearse
        binding.loginButton.setOnClickListener() {
            if (binding.authEmail.text.isNotEmpty() && binding.authPassword.text.isNotEmpty()){
                logIn()
            }
        }

        // Evento logout
        binding.logOutButton2.setOnClickListener() {
            auth.signOut();
            showUserData()
        }

        // Evento datos del usuario
        binding.checkUser.setOnClickListener()  {
            showUserData()
        }

        // Reenviar correo
        binding.resend.setOnClickListener() {
            sendEmailVerification()
        }

    }

    private fun getUserStatus() {
        val user = auth.currentUser
        if(user == null) {
            userStatus = UserStatusMode.NOT_LOGGED
        } else {
            user.reload()
            userStatus = if(user.isEmailVerified) UserStatusMode.VERIFIED else UserStatusMode.NOT_VERIFIED
        }
    }

    private fun showUserData() {
        val user = auth.currentUser
        showAlert(user?.email + " " + user?.isEmailVerified)
    }

    // Comprobar que existe un usuario
    private fun checkCurrentUser(): Boolean {
        val currentUser = auth.currentUser
        return if(currentUser == null) {
            false
        }else{
            currentUser.reload()
            true
        }
    }

    // Mostrar alertas
    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Mensaje")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        builder.show()
    }

    // Inicia la actividad Home enviando los datos del usuario
    private fun goHome(email: String) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
        }

        startActivity(homeIntent)
    }

    // Crear cuenta
    private fun createAccount() {
        val email = binding.authEmail.text.toString()
        val password = binding.authPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Enviar correo de verificación
                    sendEmailVerification()

                } else {
                    showAlert("Ha fallado la creación de la cuenta " + task.toString())
                    showUserData()
                    sendEmailVerification()
                }
            }
    }

    // Loguearse
    private fun logIn() {
        val email = binding.authEmail.text.toString()
        val password = binding.authPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    showAlert("Ha fallado la autenticación")
                }
            }
    }

    // Enviar email de verificación
    private fun sendEmailVerification() {
        auth.currentUser!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showAlert("Email enviado")
                }
            }
           .addOnFailureListener { e ->
               showAlert("Failed to send due to " + e.message)
        }
    }

    // Eliminar un usuario
    private fun deleteUser() {
        auth.currentUser!!.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        //goHome(user?.email ?: "")
        showUserData()
    }

    private fun reload() {

    }
}

