package es.murciaeduca.cprregionmurcia.registroasistencias.ui.participantes

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.adapters.ParticipanteAdapter
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.ParticipanteAsistencia
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentAsistenciaBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.util.AppDateUtil
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateFormats
import es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels.AsistenciaViewModel
import es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels.ParticipanteViewModel
import kotlinx.coroutines.launch

class AsistenciaFragment : Fragment() {

    // Parámetros SafeArgs
    private val args by navArgs<AsistenciaFragmentArgs>()

    private var _binding: FragmentAsistenciaBinding? = null
    private val binding get() = _binding!!
    private lateinit var participanteRV: RecyclerView
    private val partViewModel: ParticipanteViewModel by viewModels()
    private val asisViewModel: AsistenciaViewModel by viewModels()

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
        _binding = FragmentAsistenciaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ocultar barra de navegación
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility =
            View.GONE

        // Mostrar título e info de la sesión
        setTitleInfo()

        // Observar la asistencia
        val asistenObserver = Observer<Int> {
            binding.participantesHoyAsisten.text = it.toString()
        }
        asisViewModel.getAsisten(args.sesion.id).observe(viewLifecycleOwner, asistenObserver)

        // RecyclerView
        participanteRV = binding.participantesHoyRV
        participanteRV.layoutManager = LinearLayoutManager(context)

        // Envento click sobre el participante
        val adapter = ParticipanteAdapter {
            showAttendanceDialog(it)
        }
        participanteRV.adapter = adapter
        lifecycle.coroutineScope.launch {
            partViewModel.getAllWithAsistencia(args.sesion.codigo, args.sesion.id).collect {
                adapter.submitList(it)
            }
        }
    }

    /**
     * Gestiona el cuadro de diálogo para la asistencia de un participante
     */
    private fun showAttendanceDialog(p: ParticipanteAsistencia) {

        // No se ha marcado asistencia
        if (p.asistencia == null) {
            MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.cpr_logo)
                .setTitle("${p.nombre} ${p.apellidos}")
                .setMessage(R.string.attendance_not_set)
                .setPositiveButton(R.string.attendance_set_button) { _, _ ->
                    asisViewModel.save(args.sesion.id, p.id)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()

            // Se ha marcado asistencia
        } else {
            val msg = getString(R.string.attendance_set) + " " + AppDateUtil.dateToString(p.asistencia, DateFormats.TIME.format) + "h."
            MaterialAlertDialogBuilder(requireContext())
                .setIcon(R.drawable.cpr_logo)
                .setTitle("${p.nombre} ${p.apellidos}")
                .setMessage(msg)
                .setPositiveButton(R.string.attendance_cancel_button) { _, _ ->
                    asisViewModel.deleteById(args.sesion.id, p.id)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .show()
        }
    }

    /**
     * Muestra la información de la sesión
     */
    private fun setTitleInfo() {
        binding.actTituloHoy.text = "${args.sesion.codigo} ${args.sesion.titulo}"
        binding.actSubtituloHoy.text =
            AppDateUtil.generateDateInfo(args.sesion.inicio, args.sesion.fin)
        binding.participantesHoyTotal.text = args.sesion.num_participantes.toString()
        binding.participantesHoyAsisten.text = args.sesion.num_asisten.toString()
    }

    // Toolbar. Mostrar icono de escáner QR
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.toolbarScanQr).isVisible = true
    }

    // Evento QR escáner
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbarScanQr -> {
                val direction =
                    AsistenciaFragmentDirections.actionAsistenciaFragmentToScannerFragment(sesionId = args.sesion.id)
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