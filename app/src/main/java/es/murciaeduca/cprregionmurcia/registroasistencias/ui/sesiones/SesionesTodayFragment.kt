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
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentSesionesTodayBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateFormats
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateUtil
import es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels.SesionViewModel
import java.util.*

class SesionesTodayFragment : Fragment() {
    private var _binding: FragmentSesionesTodayBinding? = null
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
        _binding = FragmentSesionesTodayBinding.inflate(inflater, container, false)

        binding.todayDate.text = DateUtil.nowToString(DateFormats.DATE_TEXT.format)

        sesionAdapter = SesionAdapter(sesionList)
        sesionRV = binding.sesionesHoyRV
        sesionRV.adapter = sesionAdapter
        sesionRV.layoutManager = LinearLayoutManager(context)

        viewModel.getToday()
        viewModel.list.observe(viewLifecycleOwner, Observer {
            binding.sesionesHoyRV.adapter = SesionAdapter(it)
        })

        return binding.root
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
                    viewModel.insertToday()
                } catch (e: Exception) {
                    Toast.makeText(requireActivity(), "Error en la descarga", Toast.LENGTH_SHORT)
                        .show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
