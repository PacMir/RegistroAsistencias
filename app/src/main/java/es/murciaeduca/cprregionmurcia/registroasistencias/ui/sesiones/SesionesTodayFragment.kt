package es.murciaeduca.cprregionmurcia.registroasistencias.ui.sesiones

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.adapters.SesionAdapter
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentSesionesTodayBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.util.ConnUtil
import es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels.SesionViewModel
import kotlinx.coroutines.launch

class SesionesTodayFragment : Fragment() {
    private var _binding: FragmentSesionesTodayBinding? = null
    private val binding get() = _binding!!
    private lateinit var sesionRV: RecyclerView
    private val viewModel: SesionViewModel by viewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mostrar barra de navegación
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility =
            View.VISIBLE

        sesionRV = binding.sesionesHoyRV
        sesionRV.layoutManager = LinearLayoutManager(context)
        val adapter = SesionAdapter({
            requireActivity().runOnUiThread {
                val action = SesionesTodayFragmentDirections
                    .actionSesionesTodayFragmentToAsistenciaFragment(it)
                view.findNavController().navigate(action)
            }
        }, 1)
        sesionRV.adapter = adapter
        lifecycle.coroutineScope.launch {
            viewModel.getToday().collect {
                adapter.submitList(it)
            }
        }

        // Observar listado para mostrar una página vacía
        val emtpySesionesList = Observer<Int> {
            if(it == 0){
                binding.sesionesHoyEmpty.visibility = View.VISIBLE
            }else{
                binding.sesionesHoyEmpty.visibility = View.GONE
            }
        }
        viewModel.getCountToday().observe(viewLifecycleOwner, emtpySesionesList)
    }

    // Toolbar
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.toolbarDownload).isVisible = true
    }

    // Evento descarga de datos
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbarDownload -> {
                try {
                    // Comprobar conexión
                    if (!ConnUtil.checkNetwork(requireContext())) {
                        Snackbar.make(requireView(), R.string.auth_connection_error, Snackbar.LENGTH_LONG).show()
                    }else {
                        viewModel.insertToday()
                    }

                } catch (e: Exception) {
                    Snackbar.make(requireView(), e.message.toString(), Snackbar.LENGTH_LONG).show()
                }
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
