package es.murciaeduca.cprregionmurcia.registroasistencias

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import es.murciaeduca.cprregionmurcia.registroasistencias.databinding.ActivityHomeBinding
import es.murciaeduca.cprregionmurcia.registroasistencias.viewmodels.SesionViewModel


class HomeActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var binding: ActivityHomeBinding
    private val auth = Firebase.auth

    private val viewModel: SesionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vinculación de vistas
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar navegación con el componente de navegación
        // https://developer.android.com/guide/navigation/navigation-ui
        configNavigation()
    }

    // Configurar navegación
    private fun configNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerHomeView) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        // Observar el número de sesiones pasadas pendientes de enviar
        val item = binding.bottomNavigation.menu.findItem(R.id.sesionesPastFragment).itemId
        val badge = binding.bottomNavigation.getOrCreateBadge(item)
        badge.isVisible = true
        val badgeObserver = Observer<Int> {
            badge.number = it
        }
        viewModel.getNotSent().observe(this, badgeObserver)

        // Definir ambas opciones del bottom navigation como del mismo nivel jerárquico
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.sesionesTodayFragment, R.id.sesionesPastFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Deshablitar reseleción de elementos del bottom navigation
        binding.bottomNavigation.setOnItemReselectedListener {}
    }

    // Controlar la navegación hacia arriba
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Crear Toolbar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Opciones menú overflow
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            /*R.id.menuHelp -> {
                Toast.makeText(this, "Ayuda", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menuSettings -> {
                Toast.makeText(this, "Ajustes", Toast.LENGTH_SHORT).show()
                true
            }*/
            R.id.menuLogOut -> {
                auth.signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                true
            }
            else -> {
                // Habilita la navegación a elementos del menú con destinos con el mismo id
                item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
            }
        }
    }
}
