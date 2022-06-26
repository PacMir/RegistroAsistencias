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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentAuthRegisterBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.util.AppUtil
import es.murciaeduca.cprregionmurcia.registroasistencias.util.hideKeyboard

class AuthRegisterFragment : Fragment() {
    private val auth: FirebaseAuth = Firebase.auth

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    private var _binding: FragmentAuthRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
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
            if (!AppUtil.checkNetwork(requireContext())) {
                Snackbar.make(requireView(), R.string.auth_connection_error, Snackbar.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (!isEmptyFields(it) && checkPasswordField(it)) {
                createAccount(it)
            }
        }
    }

    // Crear nueva cuenta
    private fun createAccount(view: View) {

        // Comprobar contraseña
        if (checkPasswordField(view)) {

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    // Enviar correo de verificación
                    validateEmail(view)

                } else {
                    // Obtener excepciones de Firebase
                    val msg = try {
                        throw it.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        R.string.auth_account_weak_password
                    } catch (e: FirebaseAuthUserCollisionException) {
                        R.string.auth_account_exists
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        R.string.auth_email_invalid
                    } catch (e: Exception) {
                        R.string.auth_registry_error
                    }

                    Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    // Validar email
    private fun validateEmail(view: View) {
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
                            findNavController().navigate(direction)
                        }
                        .show()

                    // Guardar nombre del usuario en SharedPreferences
                    savePreferencesUserName()
                }
            }
            .addOnFailureListener {
                // Borrar usuario
                user.delete()
                Snackbar.make(view, R.string.auth_registry_error, Snackbar.LENGTH_LONG).show()
            }
    }

    // Guardar nombre del usuario en SharedPreferences
    private fun savePreferencesUserName() {
        val sp = activity?.getSharedPreferences(email, 0)?.edit()
        with(sp) {
            this?.putString("user_name", name)
            this?.apply()
        }
    }

    // Campos vacíos
    private fun isEmptyFields(view: View): Boolean {
        hideKeyboard()
        name = binding.authName.text.trim().toString()
        email = binding.authEmail.text.trim().toString()
        password = binding.authPassword.text.trim().toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Snackbar.make(view, R.string.auth_empty_error, Snackbar.LENGTH_SHORT).show()
            return true
        }

        return false
    }

    // Comprobar longitud de contraseña
    private fun checkPasswordField(view: View): Boolean {
        if (password.length < 8) {
            Snackbar.make(view, R.string.auth_password_error, Snackbar.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
