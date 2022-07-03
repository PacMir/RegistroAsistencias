package es.murciaeduca.cprregionmurcia.registroasistencias.ui.participantes

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.budiyev.android.codescanner.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentScannerBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels.AsistenciaViewModel
import es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels.ParticipanteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

enum class QrResultCodes {
    ERROR, SUCCESS, WARNING
}

class ScannerFragment : Fragment() {

    // Parámetros SafeArgs
    private val args: ScannerFragmentArgs by navArgs()

    // ViewModels
    private val viewModel: AsistenciaViewModel by viewModels()
    private val partViewModel: ParticipanteViewModel by viewModels()

    // Flag permiso de cámara
    private var cameraAccess: Boolean = true

    // Variables binding
    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!

    // Variables codescanner
    private lateinit var codeScanner: CodeScanner
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->

            // Permiso cancelado
            if (!isGranted) {
                cameraAccess = false
                showPermissionAlert()
            }
        }

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
        _binding = FragmentScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Gestión de permisos para la cámara
        permissionLauncher.launch(Manifest.permission.CAMERA)

        // Código para el escaneo
        codeScanner()
    }

    // Ocultar dropdown del toolbar
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.toolbarDropdown).isVisible = false
    }

    // Gestión del escáner QR. La cámara se activa con el evento click del botón
    // Este evalúa la variable cameraAccess y la activa o muestra la alerta al usuario
    private fun codeScanner() {
        val scannerView = binding.scannerView
        val activity = requireActivity()

        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = listOf(BarcodeFormat.QR_CODE)
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                activity.runOnUiThread {

                    try {
                        val obj = JSONObject(it.text)
                        val qrData: JSONObject = obj.getJSONObject("participante")
                        val nif = qrData.getString("id")
                        val actividad = qrData.getString("actividad")

                        // Registrar al usuario
                        registerAttendance(actividad, nif)

                    } catch (e: JSONException) {
                        showResult(getString(R.string.qr_wrong_format), QrResultCodes.ERROR)
                    }
                }

            }

            errorCallback = ErrorCallback {
                activity.runOnUiThread {
                    Snackbar.make(requireView(), "Error ${it.message}", Snackbar.LENGTH_LONG).show()
                }
            }

            // Botón de acceso a la cámara
            triggerListener()
        }
    }

    /**
     * Escucha del botón para habilitar el escaneo
     */
    fun triggerListener() {
        binding.scanTrigger.setOnClickListener {
            // Ocultar mensaje de resultado
            binding.scanResultText.isVisible = false

            if (cameraAccess) {

                // Ocultar botón e info
                hideTriggerInfo()

                // Habilitar cámara
                codeScanner.startPreview()

            } else {
                showPermissionAlert()
            }
        }
    }

    /**
     * Consulta la validez de los datos del código QR y registra la asistencia en la base de datos
     */
    private fun registerAttendance(act_codigo: String, nif: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val p = partViewModel.getWithAsistencia(act_codigo, nif, args.sesionId)

            // No existe el participante, no existe la sesión o no pertenece a la actividad
            if (p == null) {
                showResult(getString(R.string.qr_not_allowed), QrResultCodes.ERROR)

                // Ya se ha registrado una asistencia anteriormente
            } else if (p.asistencia != null) {
                val msg = getString(R.string.qr_already_checked) + "\n${p.nombre} ${p.apellidos}"
                showResult(msg, QrResultCodes.ERROR)

            } else {
                try {
                    viewModel.saveFromQr(p, args.sesionId)

                    val msg = getString(R.string.qr_checked) + "\n${p.nombre} ${p.apellidos}"
                    showResult(msg, QrResultCodes.SUCCESS)

                } catch (e: Exception) {
                    showResult(getString(R.string.qr_not_allowed), QrResultCodes.ERROR)
                }

            }
        }
    }

    /**
     * Muestra el resultado del escaneo con color en función del tipo
     */
    private fun showResult(msg: String, code: QrResultCodes) {
        requireActivity().runOnUiThread {
            val result = binding.scanResultText

            // Código de error
            result.background = when (code) {
                QrResultCodes.ERROR -> ContextCompat.getDrawable(requireContext(),
                    R.drawable.qr_callback_shape_error)
                QrResultCodes.WARNING -> ContextCompat.getDrawable(requireContext(),
                    R.drawable.qr_callback_shape_warning)
                else -> ContextCompat.getDrawable(requireContext(),
                    R.drawable.qr_callback_shape_success)
            }

            // Mostrar botón y mensaje
            showTriggerInfo()

            result.text = msg
            result.isVisible = true
        }
    }

    /**
     * Funciones para mostrar/ocultar botón e info
     */
    private fun showTriggerInfo(){
        binding.scanTrigger.visibility = View.VISIBLE
        binding.scanInfo.visibility = View.VISIBLE
    }

    private fun hideTriggerInfo(){
        binding.scanTrigger.visibility = View.INVISIBLE
        binding.scanInfo.visibility = View.INVISIBLE
    }

    /**
     * Muestra la alerta de permiso de cámara y vuelve al fragmento anterior
     */
    private fun showPermissionAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.cpr_logo)
            .setTitle(R.string.permission_camera_title)
            .setMessage(R.string.permission_camera)
            .setPositiveButton(R.string.accept, null)
            .setOnDismissListener {
                findNavController().popBackStack()
            }.show()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}