package es.murciaeduca.cprregionmurcia.registroasistencias.ui.participantes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentAsistenciaBinding

class AsistenciaFragment : Fragment() {
    private var _binding: FragmentAsistenciaBinding? = null
    private val binding get() = _binding!!
    private lateinit var actCodigo: String

    // Informar al sistema que el fragmento de la barra de la app participa en la propagación del menú de opciones
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            actCodigo = it.getString("actCodigo").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Vinculación de vistas
        _binding = FragmentAsistenciaBinding.inflate(inflater, container, false)
        return binding.root
    }

}