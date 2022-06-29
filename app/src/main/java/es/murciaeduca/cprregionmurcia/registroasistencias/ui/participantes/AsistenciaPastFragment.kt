package es.murciaeduca.cprregionmurcia.registroasistencias.ui.participantes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.adapters.ParticipanteAdapter
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentAsistenciaPastBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels.ActividadViewModel
import es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels.ParticipanteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class AsistenciaPastFragment : Fragment() {

    // Parámetros
    companion object {
        var ACT_CODIGO = "actCodigo"
        var SES_INICIO = "sesInicio"
    }

    private var _binding: FragmentAsistenciaPastBinding? = null
    private val binding get() = _binding!!
    private lateinit var participanteRV: RecyclerView
    private val viewModel: ParticipanteViewModel by viewModels()
    private val actViewModel: ActividadViewModel by viewModels()

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
        _binding = FragmentAsistenciaPastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ocultar barra de navegación
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility =
            View.GONE

        // Título actividad
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            val actividad = actViewModel.getById(actCodigo)
            binding.actTitulo.text = actividad.codigo + " " + actividad.titulo
        }

        // RecyclerView
        participanteRV = binding.participantesRV
        participanteRV.layoutManager = LinearLayoutManager(context)
        val adapter = ParticipanteAdapter {
            Toast.makeText(context, "Hola", Toast.LENGTH_LONG).show()
        }
        participanteRV.adapter = adapter
        lifecycle.coroutineScope.launch {
            viewModel.getAllInActividad(actCodigo, sesInicio).collect {
                adapter.submitList(it)
            }
        }
    }

    // Toolbar
    /*override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}