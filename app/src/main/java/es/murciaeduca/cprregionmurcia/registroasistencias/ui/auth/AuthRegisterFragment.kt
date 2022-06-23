package es.murciaeduca.cprregionmurcia.registroasistencias.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.application.App
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.UsuarioEntity
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentAuthRegisterBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Fragmento
 * Autenticación: Login
 */
class AuthRegisterFragment : Fragment() {
    private val userDao = App.getInstance().usuarioDao()
    private val viewModel: AuthViewModel by activityViewModels()
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var navController: NavController

    private var _binding: FragmentAuthRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        navController = findNavController()

        // Vinculación de vistas
        _binding = FragmentAuthRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Establecer listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListeners(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Listeners
    private fun clickListeners(view: View) {
        // Evento registro
        binding.signInButton.setOnClickListener {
            // Comprobar conexión
            if (!AppUtils.checkNetwork(requireContext())) {
                Snackbar.make(view, R.string.auth_error_connection, Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (binding.authEmail.text.isNotEmpty() && binding.authPassword.text.isNotEmpty()) {
                createAccount(view)
            }
        }
    }

    // Crear nueva cuenta
    private fun createAccount(view: View) {
        val name = binding.authName.text.toString()
        val lastName = binding.authLastName.text.toString()
        val email = binding.authEmail.text.toString()
        val password = binding.authPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Crear usuario en la base de datos
                    try {
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                userDao.save(UsuarioEntity(email, name, lastName))
                            }
                        }

                        // Enviar correo de verificación
                        sendEmailVerification(view, email)

                        // Borrar usuario de firebase en caso de error
                    } catch (e: Exception) {
                        Snackbar.make(view, R.string.auth_error_registry, Snackbar.LENGTH_LONG)
                            .show()
                        auth.currentUser?.delete()
                    }

                } else {
                    Snackbar.make(view, R.string.auth_error_registry, Snackbar.LENGTH_LONG).show()
                }
            }
    }

    // Enviar email de verificación
    private fun sendEmailVerification(view: View, email: String) {
        val user = auth.currentUser!!

        user.sendEmailVerification()
            .addOnCompleteListener {
                // Mostrar mensaje de validación
                if (it.isSuccessful) {
                    viewModel.goVerify(requireContext(), navController, email)
                }
            }
            .addOnFailureListener {
                // Borrar usuario
                user.delete()
                Snackbar.make(view, R.string.auth_error_registry, Snackbar.LENGTH_LONG).show()
            }

    }

/*
// Comprobar email
    private fun checkEmptyFields(str: String): Boolean {
        if (str.isEmpty()) {
            Toast.makeText(requireContext(), R.string.auth_error_empty, Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    // Comprobar contraseña
    private fun checkPasswordField(): Boolean {
        if (!checkEmptyFields(password)) {
            return false
        }

        if (password.length < 8) {
            Toast.makeText(requireContext(), R.string.auth_error_password, Toast.LENGTH_SHORT)
                .show()
            return false
        }

        return true
    }
    */
}


