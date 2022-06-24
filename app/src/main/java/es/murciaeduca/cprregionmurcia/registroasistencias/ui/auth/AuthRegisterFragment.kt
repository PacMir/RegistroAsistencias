package es.murciaeduca.cprregionmurcia.registroasistencias.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var navController: NavController

    private lateinit var name: String
    private lateinit var lastName: String
    private lateinit var email: String
    private lateinit var password: String

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

        clickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Listeners
    private fun clickListeners() {
        // Evento registro
        binding.signInButton.setOnClickListener {

            // Comprobar conexión
            if (!AppUtils.checkNetwork(requireContext())) {
                Snackbar.make(requireView(), R.string.auth_error_connection, Snackbar.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            name = binding.authName.text.trim().toString()
            lastName = binding.authLastName.text.trim().toString()
            email = binding.authEmail.text.trim().toString()
            password = binding.authPassword.text.trim().toString()

            if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Snackbar.make(requireView(), R.string.auth_error_empty, Snackbar.LENGTH_SHORT)
                    .show()

            } else {

                // Comprobar si existe el usuario
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        if (userDao.userExists(email) > 0) {
                            Snackbar.make(
                                requireView(),
                                R.string.auth_account_exists,
                                Snackbar.LENGTH_LONG
                            )
                                .show()

                        } else {
                            createAccount()
                        }
                    }
                }
            }
        }
    }

    // Crear nueva cuenta
    private fun createAccount() {

        // Comprobar contraseña
        if (checkPasswordField()) {

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        // Crear usuario en la base de datos
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {

                                try {
                                    userDao.save(UsuarioEntity(email, name, lastName))

                                    // Enviar correo de verificación
                                    sendEmailVerification()

                                    // Borrar usuario de firebase en caso de error
                                } catch (e: Exception) {
                                    Snackbar.make(
                                        requireView(),
                                        R.string.auth_error_registry,
                                        Snackbar.LENGTH_LONG
                                    )
                                        .show()
                                    auth.currentUser!!.delete()
                                }
                            }
                        }

                    } else {
                        Snackbar.make(
                            requireView(),
                            R.string.auth_error_registry,
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    }
                }
        }
    }

    // Enviar email de verificación
    private fun sendEmailVerification() {
        val user = auth.currentUser!!

        user.sendEmailVerification()
            .addOnCompleteListener {
                // Mostrar mensaje de validación y redirección a login
                if (it.isSuccessful) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(context?.resources?.getString(R.string.auth_verify_title) + " " + email)
                        .setMessage(R.string.auth_verify)
                        .setPositiveButton(R.string.accept) { _, _ ->
                            val direction =
                                AuthRegisterFragmentDirections.actionAuthRegisterFragmentToAuthLoginFragment()
                            navController.navigate(direction)
                        }
                        .show()
                }
            }
            .addOnFailureListener {
                // Borrar usuario
                user.delete()
                Snackbar.make(requireView(), R.string.auth_error_registry, Snackbar.LENGTH_LONG)
                    .show()
            }
    }

    // Comprobar contraseña
    private fun checkPasswordField(): Boolean {
        if (password.length < 8) {
            Snackbar.make(requireView(), R.string.auth_error_password, Snackbar.LENGTH_SHORT)
                .show()
            return false
        }

        return true
    }
}


