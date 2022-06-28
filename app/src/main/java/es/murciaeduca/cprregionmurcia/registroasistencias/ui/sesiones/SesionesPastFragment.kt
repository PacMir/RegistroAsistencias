package es.murciaeduca.cprregionmurcia.registroasistencias.ui.sesiones

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.adapters.SesionAdapter
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.entities.SesionActividad
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentSesionesPastBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels.SesionViewModel

class SesionesPastFragment : Fragment() {
    private var _binding: FragmentSesionesPastBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SesionViewModel by viewModels()
    private lateinit var sesionRV: RecyclerView
    private lateinit var sesionAdapter: SesionAdapter
    private val sesionList: List<SesionActividad> = ArrayList()

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

        sesionAdapter = SesionAdapter(sesionList)
        sesionRV = binding.sesionesPastRV
        sesionRV.adapter = sesionAdapter
        sesionRV.layoutManager = LinearLayoutManager(context)

        viewModel.getPast()
        viewModel.list.observe(viewLifecycleOwner, Observer {
            binding.sesionesPastRV.adapter = SesionAdapter(it)
        })

        return binding.root
    }

    // Toolbar
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.toolbarSearch)
        menu.setGroupVisible(item.groupId, true)
    }

    // Evento descarga de datos
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
}
