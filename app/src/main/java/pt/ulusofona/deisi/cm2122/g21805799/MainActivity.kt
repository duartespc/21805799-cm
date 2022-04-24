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


class MainActivity : AppCompatActivity(), LocationListener {

    private val model = DataManager
    private lateinit var binding: ActivityMainBinding


    var locationManager: LocationManager? = null
    private val GPS_TIME_INTERVAL = 1000 * 60 * 5 // get gps location every 1 min

    private val GPS_DISTANCE = 1000 // set the distance value in meter

    private val HANDLER_DELAY = 1000 * 60 * 5 // 5 minutes
    private val START_HANDLER_DELAY = 0

    val PERMISSIONS = arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    val PERMISSION_ALL = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL)
        }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                requestLocation()
                handler.postDelayed(this, HANDLER_DELAY.toLong())
            }
        }, START_HANDLER_DELAY.toLong())
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

    override fun onLocationChanged(location: Location) {
        Log.d("mylog", "Got Location: " + location.latitude + ", " + location.longitude)
        Toast.makeText(this@MainActivity, "Got Coordinates: " + location.latitude + ", " + location.longitude, Toast.LENGTH_SHORT).show()
        locationManager!!.removeUpdates(this)
    }

    private fun requestLocation() {
        if (locationManager == null) locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        GPS_TIME_INTERVAL.toLong(), GPS_DISTANCE.toFloat(), this)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed(object : Runnable {
                override fun run() {
                    requestLocation()
                    handler.postDelayed(this, HANDLER_DELAY.toLong())
                }
            }, START_HANDLER_DELAY.toLong())
        } else {
            finish()
        }
    }

}