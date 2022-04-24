package pt.ulusofona.deisi.cm2122.g21805799

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import pt.ulusofona.deisi.cm2122.g21805799.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val model = DataManager
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.toolbar.title = getString(R.string.app_name)
        binding.toolbar.subtitle = "${getString(R.string.risk)} ${getString(R.string.risk_for_this_concelho)}"
        setContentView(binding.root)
        if (!screenRotated(savedInstanceState)) {
            NavigationManager.goToDashboardFragment(supportFragmentManager)
        }
        model.onStart()
    }

    private fun screenRotated(savedInstanceState: Bundle?): Boolean {
        return savedInstanceState != null
    }

    override fun onStart() {
        super.onStart()
        setSupportActionBar(binding.toolbar)
        setupDrawerMenu()

    }

    private fun setupDrawerMenu(){
        val toggle = ActionBarDrawerToggle(this,
                binding.drawer, binding.toolbar,
                R.string.drawer_open, R.string.drawer_closed
        )
        binding.navDrawer.setNavigationItemSelectedListener {
            onClickNavigationItem(it)
        }
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun onClickNavigationItem(item: MenuItem): Boolean{
        when(item.itemId) {
            R.id.nav_list -> NavigationManager.goToFiresListFragment(supportFragmentManager)
            R.id.nav_dashboard -> NavigationManager.goToDashboardFragment(supportFragmentManager)
            R.id.nav_form -> NavigationManager.goToFireFormFragment(supportFragmentManager)
            R.id.nav_about -> NavigationManager.goToAboutFragment(supportFragmentManager)
            R.id.nav_map -> NavigationManager.goToFiresMapFragment(supportFragmentManager)
        }
        binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        when {
            binding.drawer.isDrawerOpen(GravityCompat.START) -> binding.drawer.closeDrawer(GravityCompat.START)
            supportFragmentManager.backStackEntryCount == 1 -> finish()
            else -> super.onBackPressed()
        }
    }


}