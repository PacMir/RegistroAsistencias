package es.murciaeduca.cprregionmurcia.registroasistencias.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.HomeActivity
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentAuthLoginBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.utils.AppUtils
import es.murciaeduca.cprregionmurcia.registroasistencias.utils.hideKeyboard

/**
 * Fragmento
 * Autenticación: Login
 */
class AuthLoginFragment : Fragment() {
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var navController: NavController

    private var _binding: FragmentAuthLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        navController = findNavController()

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
            hideKeyboard()

            // Comprobar conexión
            if (!AppUtils.checkNetwork(requireContext())) {
                Snackbar.make(requireView(), R.string.auth_error_connection, Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            logIn()
        }

        // Ir a fragment de registro
        binding.signInLink.setOnClickListener {
            val direction =
                AuthLoginFragmentDirections.actionAuthLoginFragmentToAuthRegisterFragment()
            navController.navigate(direction)
        }
    }

    // Iniciar sesión
    private fun logIn() {
        val email = binding.authEmail.text.trim().toString()
        val password = binding.authPassword.text.trim().toString()

        // Campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Snackbar.make(requireView(), R.string.auth_error_empty, Snackbar.LENGTH_SHORT).show()

            // Intentar autenticación con Firebase
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        // Usuario pendiente de verificación
                        if (!auth.currentUser!!.isEmailVerified) {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle(R.string.auth_email_verification_title)
                                .setMessage(R.string.auth_email_verification)
                                // TODO
                                //.setNeutralButton(R.string.auth_resend_email, null)
                                .setPositiveButton(R.string.accept, null)
                                .show()

                            // Nueva autenticación
                            auth.signOut()

                            // Usuario verificado, redirección a Home
                        } else {
                            startActivity(Intent(activity, HomeActivity::class.java))
                            activity?.finish()
                        }

                    } else {
                        Snackbar.make(requireView(), R.string.auth_error_login, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}