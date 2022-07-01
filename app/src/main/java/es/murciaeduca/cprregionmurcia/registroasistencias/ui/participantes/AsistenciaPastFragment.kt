package es.murciaeduca.cprregionmurcia.registroasistencias.ui.participantes

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.adapters.ParticipanteAdapter
import es.murciaeduca.cprregionmurcia.registroasistencias.data.database.ParticipanteAsistencia
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentAsistenciaPastBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.util.AppDateUtil
import es.murciaeduca.cprregionmurcia.registroasistencias.util.ConnUtil
import es.murciaeduca.cprregionmurcia.registroasistencias.util.DateFormats
import es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels.ParticipanteViewModel
import es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels.SesionViewModel
import kotlinx.coroutines.launch


class AsistenciaPastFragment : Fragment() {

    // Parámetros SafeArgs
    private val args by navArgs<AsistenciaPastFragmentArgs>()

    private var _binding: FragmentAsistenciaPastBinding? = null
    private val binding get() = _binding!!
    private lateinit var participanteRV: RecyclerView
    private val partViewModel: ParticipanteViewModel by viewModels()
    private val sesViewModel: SesionViewModel by viewModels()

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
        _binding = FragmentAsistenciaPastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ocultar barra de navegación
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation).visibility =
            View.GONE

        // Mostrar título e info de la sesión
        setTitleInfo()

        // RecyclerView
        participanteRV = binding.participantesPastRV
        participanteRV.layoutManager = LinearLayoutManager(context)

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
     * Muestra el diálogo con la asistencia del participante
     */
    private fun showAttendanceDialog(p: ParticipanteAsistencia) {
        var dialogText: String
        if (p.asistencia == null) {
            dialogText = getString(R.string.attendance_not_set)
        } else {
            dialogText = getString(R.string.attendance_set) + AppDateUtil.dateToString(p.asistencia,
                DateFormats.TIME.format) + "h."
            dialogText += if (p.tipo_registro == 1) {
                getString(R.string.attendance_qr_method)

            } else
                getString(R.string.attendance_manual_method)
        }

        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.cpr_logo)
            .setTitle("${p.nombre} ${p.apellidos}")
            .setMessage(dialogText)
            .show()
    }


    /**
     * Muestra la información de la sesión
     */
    private fun setTitleInfo() {
        binding.actTituloPast.text = "${args.sesion.codigo} ${args.sesion.titulo}"
        binding.actSubtituloPast.text =
            AppDateUtil.generateDateInfo(args.sesion.inicio, args.sesion.fin)
        binding.participantesPastTotal.text = args.sesion.num_participantes.toString()
        binding.participantesPastAsisten.text = args.sesion.num_asisten.toString()

        if (args.sesion.upload == null) {
            binding.iconCloudOff.visibility = View.VISIBLE
            binding.iconCloudOff.text = getText(R.string.attendance_not_sent)

        } else {
            binding.iconCloudDone.visibility = View.VISIBLE
            binding.iconCloudDone.text = AppDateUtil.dateToString(args.sesion.upload, DateFormats.DATE_TIME.format)
        }

    }

    // Toolbar
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (args.sesion.upload == null) {
            menu.findItem(R.id.toolbarUpload).isVisible = true
        }
    }

    // Evento envío de datos
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.toolbarUpload -> {
                try {
                    // Comprobar conexión
                    if (!ConnUtil.checkNetwork(requireContext())) {
                        Snackbar.make(requireView(),
                            R.string.auth_connection_error,
                            Snackbar.LENGTH_LONG).show()

                    } else {
                        sesViewModel.send(args.sesion.id)
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
