package es.murciaeduca.cprregionmurcia.registroasistencias.ui.participantes

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentAsistenciaBinding
import java.util.*

class AsistenciaFragment : Fragment() {
    // Parámetros
    companion object {
        var ACT_CODIGO = "actCodigo"
        var SES_INICIO = "sesInicio"
    }

    private var _binding: FragmentAsistenciaBinding? = null
    private val binding get() = _binding!!

    // lateinit no soporta tipos primitivos
    private lateinit var sesInicio: Date
    private lateinit var actCodigo: String

    // Informar al sistema que el fragmento de la barra de la app participa en la propagación del menú de opciones
    // Recibir parámetros
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            actCodigo = it.getString(ACT_CODIGO).toString()
            sesInicio = Date(it.getLong(SES_INICIO))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Vinculación de vistas
        _binding = FragmentAsistenciaBinding.inflate(inflater, container, false)

        // Ocultar barra de navegación
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility =
            View.GONE

        return binding.root
    }

    // Toolbar
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.toolbarScanQr).isVisible = true
    }

    // Evento QR escáner
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbarScanQr -> {
                val direction =
                    AsistenciaPastFragmentDirections.actionAsistenciaPastFragmentToScannerFragment()
                findNavController().navigate(direction)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}