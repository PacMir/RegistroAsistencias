package es.murciaeduca.cprregionmurcia.registroasistencias.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentAuthLoginBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.util.hideKeyboard

class AuthLoginFragment : Fragment() {
    private val auth: FirebaseAuth = Firebase.auth

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
        return binding.root
    }

    // Establecer listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Listeners
    private fun clickListeners() {

        // Evento login
        binding.logInButton.setOnClickListener {
            // Comprobar campos
            if (!isEmptyFields(it)) {
                logIn(it)
            }
        }

        // Evento ir a registro
        binding.signInLink.setOnClickListener {
            val direction =
                AuthLoginFragmentDirections.actionAuthLoginFragmentToAuthRegisterFragment()
            findNavController().navigate(direction)
        }
    }

    // Iniciar sesión
    fun logIn(view: View) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser!!

                    // Usuario pendiente de verificación
                    if (!user.isEmailVerified) {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(R.string.auth_email_verification_title)
                            .setMessage(R.string.auth_email_verification)
                            .setNeutralButton(R.string.accept, null)
                            // Reenvío de correo de verificación
                            .setPositiveButton(R.string.auth_email_resend) { _, _ ->
                                user.sendEmailVerification()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            Snackbar.make(view,
                                                R.string.auth_email_success,
                                                Snackbar.LENGTH_LONG).show()
                                        }
                                    }
                                    .addOnFailureListener {
                                        Snackbar.make(view,
                                            R.string.auth_email_error,
                                            Snackbar.LENGTH_LONG).show()
                                    }
                            }.show()

                        // Nueva autenticación
                        auth.signOut()

                        // Usuario verificado, redirección a Home
                    } else {

                        // Redirección a Home
                        val direction =
                            AuthLoginFragmentDirections.actionAuthLoginFragmentToHomeActivity()
                        findNavController().navigate(direction)

                    }

                } else {
                    Snackbar.make(view, R.string.auth_login_error, Snackbar.LENGTH_SHORT).show()
                }
            }
    }

    // Campos vacíos
    private fun isEmptyFields(view: View): Boolean {
        hideKeyboard()
        email = binding.authEmail.text.trim().toString()
        password = binding.authPassword.text.trim().toString()

        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(view, R.string.auth_empty_error, Snackbar.LENGTH_SHORT).show()
            return true
        }

        return false
    }
}
