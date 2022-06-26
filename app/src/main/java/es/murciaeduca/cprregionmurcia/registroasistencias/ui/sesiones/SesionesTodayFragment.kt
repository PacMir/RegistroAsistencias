package es.murciaeduca.cprregionmurcia.registroasistencias.ui.sesiones

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentSesionesTodayBinding

class SesionesTodayFragment : Fragment() {
    private var _binding: FragmentSesionesTodayBinding? = null
    private val binding get() = _binding!!

    // Informar al sistema que el fragmento de la barra de la app participa en la propagación del menú de opciones
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Vinculación de vistas
        _binding = FragmentSesionesTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Establecer listeners
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
    }

    // Listeners
    private fun clickListeners() {
        // Click en sesión
        binding.showPart.setOnClickListener {
            val direction =
                SesionesTodayFragmentDirections.actionSesionesTodayFragmentToAsistenciaFragment()
            findNavController().navigate(direction)
        }
    }

    // Toolbar
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.toolbarDownload)
        item.isVisible = true
    }

    // Evento descarga de datos
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbarDownload -> {
                Toast.makeText(requireActivity(), "Descarga", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
