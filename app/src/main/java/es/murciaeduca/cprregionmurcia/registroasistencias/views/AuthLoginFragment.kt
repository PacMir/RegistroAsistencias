package es.murciaeduca.cprregionmurcia.registroasistencias.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentAuthLoginBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.utils.AppUtils

/**
 * Fragmento
 * Autenticación: Login
 */
class AuthLoginFragment : Fragment() {
    private val auth: FirebaseAuth = Firebase.auth
    private val utils: AppUtils = AppUtils()
    private lateinit var email: String
    private lateinit var password: String
    private var _binding: FragmentAuthLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        // Vinculación de vistas
        _binding = FragmentAuthLoginBinding.inflate(inflater, container, false)

        // Eventos click
        clickListeners()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Configuración de listeners
    private fun clickListeners() {
        // Evento login
        binding.logInButton.setOnClickListener {
            email = binding.authEmail.text.trim().toString()
            password = binding.authPassword.text.trim().toString()

            if (!checkEmailField() || !checkPasswordField()) {
                return@setOnClickListener
            }

            logIn()
        }

        // Ir a fragment de registro
        binding.signInLink.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.authRegisterFragment)
        }
    }

    // Comprobar email
    private fun checkEmailField(): Boolean {
        if (email.isEmpty()) {
            binding.authEmail.error = getString(R.string.auth_error_empty)
            binding.authEmail.requestFocus()

            return false
        }

        return true
    }

    // Comprobar contraseña
    private fun checkPasswordField(): Boolean {
        if (password.isEmpty()) {
            binding.authPassword.error = getString(R.string.auth_error_empty)
            binding.authPassword.requestFocus()

            return false
        }

        if (password.length < 8) {
            binding.authPassword.error = getString(R.string.auth_error_password)
            binding.authPassword.requestFocus()

            return false
        }

        return true
    }

    // Iniciar sesión
    private fun logIn() {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    activity.let {
                        val intent = Intent(it, HomeActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    utils.showAlert(requireContext(), "Ha fallado la autenticación")
                }
            }
    }
/*
    // Enviar/reenviar email de verificación
    private fun sendEmailVerification() {
        // Comprobar que han pasado más de 10 minutos desde el último correo
        if (userStatus == UserStatusMode.NOT_VERIFIED && getMinutesAfterEmailSent() < 10) {
            showAlert("Por favor, espere ${AuthActivity.MIN_MINUTES} minutos para volver a solicitar otro correo de confirmación.")

        } else {

            val user = auth.currentUser!!
            user.sendEmailVerification()
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        // Guardar la fecha y hora a la que se envió el correo, en milisegundos, en las preferencias de la actividad
                        with(sp.edit()) {
                            putString("userEmail", user.email)
                            putLong("lastEmailSent", java.util.Calendar.getInstance().timeInMillis)
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
    }

    // Calcular el tiempo que ha pasado desde que se envió el email
    private fun getMinutesAfterEmailSent(): Long {
        val now = Calendar.getInstance().timeInMillis
        val lastEmailSent = sp.getLong("lastEmailSent", now)

        return (now - lastEmailSent) / 60000
    }*/
}