package es.murciaeduca.cprregionmurcia.registroasistencias.ui.sesiones

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.adapters.SesionAdapter
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentSesionesTodayBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels.SesionViewModel
import kotlinx.coroutines.Dispatchers
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

        sesionRV = binding.sesionesHoyRV
        sesionRV.layoutManager = LinearLayoutManager(context)
        val adapter = SesionAdapter {
            val action = SesionesTodayFragmentDirections
                .actionSesionesTodayFragmentToAsistenciaFragment(
                    actCodigo = it.codigo,
                    sesInicio = it.inicio.time
                )
            view.findNavController().navigate(action)
        }
        sesionRV.adapter = adapter
        lifecycle.coroutineScope.launch {
            viewModel.getToday().collect() {
                adapter.submitList(it)
            }
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
                try {
                    lifecycle.coroutineScope.launch(Dispatchers.IO) {
                        viewModel.insertToday()
                    }

                } catch (e: Exception) {
                    Toast.makeText(requireActivity(), e.message, Toast.LENGTH_LONG)
                        .show()
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
