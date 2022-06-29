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
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import es.murciaeduca.cprregionmurcia.registroasistencias.R
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.FragmentScannerBinding
import org.json.JSONException
import org.json.JSONObject

class ScannerFragment : Fragment() {
    private var cameraAccess: Boolean = true
    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!
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

    // Gestioón del escáner QR. La cámara se activa con el evento click del botón
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
                    val result = binding.scanResultText
                    try {
                        val obj = JSONObject(it.text)
                        val participante: JSONObject = obj.getJSONObject("participante")
                        val id = participante.getString("id")
                        val actividad = participante.getString("actividad")

                        // Comprobar que existe el participante, la sesión está activa y no ha sido marcado
                        //TODO

                        result.text = id

                    } catch (e: JSONException) {
                        result.background = (ContextCompat.getDrawable(requireContext(),
                            R.drawable.qr_callback_shape_error))
                        result.text = R.string.qr_wrong_format.toString()
                    }

                    result.isVisible = true
                }
            }

            errorCallback = ErrorCallback {
                activity.runOnUiThread {
                    Snackbar.make(requireView(), "Error ${it.message}", Snackbar.LENGTH_LONG).show()
                }
            }

            // Botón de acceso a la cámara
            binding.scanTrigger.setOnClickListener {
                binding.scanResultText.isVisible = false
                if(cameraAccess) {
                    codeScanner.startPreview()
                }else{
                    showPermissionAlert()
                }
            }
        }
    }

    // Muestra un diálogo de alerta y vuelve al fragmento anterior
    private fun showPermissionAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.permission_camera_title)
            .setMessage(R.string.permission_camera)
            .setPositiveButton(R.string.accept) { _, _ ->
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