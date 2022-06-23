package es.murciaeduca.cprregionmurcia.registroasistencias.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import es.murciaeduca.cprregionmurcia.registroasistencias.R


enum class UserStatusMode {
    NOT_LOGGED, NOT_VERIFIED, VERIFIED
}

class AuthViewModel : ViewModel() {

    // Inicializar el estado del usuario como no logueado
    private var userStatus = UserStatusMode.NOT_LOGGED

    // Obtener el estado del usuario
    fun getUserStatus(auth: FirebaseAuth): UserStatusMode {
        val user = auth.currentUser
        userStatus = if (user == null) {
            UserStatusMode.NOT_LOGGED

        } else {
            user.reload()
            if (user.isEmailVerified) UserStatusMode.VERIFIED else UserStatusMode.NOT_VERIFIED
        }

        return userStatus
    }

    // Alerta de cuenta pendiente de verificación
    fun notVerified(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setMessage(R.string.auth_email_verification)
            // TODO
            .setPositiveButton("Reenviar correo", null)
            .setNeutralButton("Aceptar", null)
            .show()
    }

    // Alerta envío de verificación
    fun goVerify(context: Context, navController: NavController, email: String) {
        MaterialAlertDialogBuilder(context)
            .setMessage("Se ha enviado un correo a $email Por favor, haga clic en el enlace proporcionado para verificar su cuenta.")
            .setPositiveButton("Aceptar") { _, _ ->
                val direction =
                    AuthRegisterFragmentDirections.actionAuthRegisterFragmentToAuthLoginFragment()
                navController.navigate(direction)
            }
            .show()
    }
}