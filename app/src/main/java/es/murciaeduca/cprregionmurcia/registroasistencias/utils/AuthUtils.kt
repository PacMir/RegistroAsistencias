package es.murciaeduca.cprregionmurcia.registroasistencias.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AlertDialog

enum class UserStatusMode {
    NOT_LOGGED, NOT_VERIFIED, VERIFIED
}

class AppUtils {

    // Comprobar conexión
    fun checkNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    // Mostrar alertas
    fun showAlert(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Atención")
        builder.setMessage(message)
        builder.setPositiveButton("Cerrar", null)
        builder.show()
    }

}