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
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentSesionesPastBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels.SesionViewModel
import kotlinx.coroutines.launch

class SesionesPastFragment : Fragment() {
    private var _binding: FragmentSesionesPastBinding? = null
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
        _binding = FragmentSesionesPastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sesionRV = binding.sesionesPastRV
        sesionRV.layoutManager = LinearLayoutManager(context)
        val adapter = SesionAdapter {
            val action =
                SesionesPastFragmentDirections.actionSesionesPastFragmentToAsistenciaPastFragment(
                    actCodigo = it.codigo,
                    sesInicio = it.inicio.time
                )
            view.findNavController().navigate(action)
        }
        sesionRV.adapter = adapter
        lifecycle.coroutineScope.launch{
            viewModel.getPast().collect {
                adapter.submitList(it)
            }
        }
    }

    // Toolbar
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.toolbarSearch)
        menu.setGroupVisible(item.groupId, true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbarUpload -> {
                Toast.makeText(requireActivity(), "Enviar", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.toolbarSearch -> {
                Toast.makeText(requireActivity(), "Buscar", Toast.LENGTH_SHORT).show()
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
