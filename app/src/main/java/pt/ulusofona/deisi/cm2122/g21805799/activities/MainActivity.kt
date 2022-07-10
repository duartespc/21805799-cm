package pt.ulusofona.deisi.cm2122.g21805799.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21805799.NavigationManager
import pt.ulusofona.deisi.cm2122.g21805799.R
import pt.ulusofona.deisi.cm2122.g21805799.data.FiresRepository
import pt.ulusofona.deisi.cm2122.g21805799.databinding.ActivityMainBinding
import pt.ulusofona.deisi.cm2122.g21805799.model.DataManager
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val model: DataManager = FiresRepository.getInstance()
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    private lateinit var lastRisk: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lastRisk = getString(R.string.not_available)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.toolbar.title = getString(R.string.app_name)
        binding.toolbar.subtitle = "${getString(R.string.risk)} ${getString(R.string.not_available)}"
        setContentView(binding.root)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        handler.postDelayed(highBatteryLooper, 20000)

        if (!screenRotated(savedInstanceState)) {
            NavigationManager.goToDashboardFragment(supportFragmentManager)
        }

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    fun getLocation(onFinished: (String) -> Unit) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)

                        if (!list.isEmpty()) {
                            model.getRisk((list[0].locality)) {
                                onFinished(it)
                            }
                        } else {
                            Log.i("APP", "model.getRisk is EMPTY")
                        }
                    }
                    else {
                        Log.i("APP","Location == null")
                    }
                }
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(highBatteryLooper)
        handler.removeCallbacks(lowBatteryLooper)
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
            R.id.nav_map -> NavigationManager.goToMapFragment(supportFragmentManager)
            R.id.nav_settings -> NavigationManager.goToSettingsFragment(supportFragmentManager)
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

    // This looper always updates risk according to location - disreguarding battery level - DEFAULT
    val handler = Handler(Looper.getMainLooper())
    val highBatteryLooper = object: Runnable {
        override fun run() {
            getLocation {
                lastRisk = it
                CoroutineScope(Dispatchers.Main).launch {
                    binding.toolbar.subtitle = "${getString(R.string.risk)} $lastRisk"
                }
                Log.i("APP", "HIGH BatteryLooper, risk: $lastRisk,   delay: 20000")
                handler.postDelayed(this, 20000)
            }
            //binding.toolbar.subtitle = "${getString(R.string.risk)} $lastRisk"

        }
    }

    // This looper only updates risk according to location if battery percentage > 20%
    // If battery is lower than 20% check again in 1m

    val lowBatteryLooper = object: Runnable {
        override fun run() {
            // Call battery manager service
            val bm = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager

            // Get the battery percentage and store it in a INT variable
            val batLevel:Int = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

            // Only update location if battery level is good (20%)
            // ??? If it's not, set state to gray ????

            if (batLevel > 20) {
                getLocation {
                    lastRisk = it
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.toolbar.subtitle = "${getString(R.string.risk)} $lastRisk"
                        binding.toolbar.setSubtitleTextColor(getColor(R.color.white))
                    }
                    Log.i("APP", "LOW BatteryLooper, Risk: $lastRisk,   delay: 20000")
                    handler.postDelayed(this, 20000)
                }
            } else {
                Log.i("APP", "LOW BatteryLooper, battery < 20%,   delay: 20000")
                binding.toolbar.setSubtitleTextColor(getColor(R.color.gray))
                handler.postDelayed(this, 20000)
            }

        }
    }

    fun startLowBatteryLooper() {
        Log.i("APP", "Started low battery Looper, delay: 20000!")
        handler.postDelayed(lowBatteryLooper, 20000)
    }

    fun stopLowBatteryLooper() {
        Log.i("APP", "Stopped low battery Looper!")
        handler.removeCallbacks(lowBatteryLooper)
    }

    fun startHighBatteryLooper() {
        Log.i("APP", "Started low battery Looper, delay: 20000!")
        handler.postDelayed(highBatteryLooper, 20000)
    }

    fun stopHighBatteryLooper() {
        Log.i("APP", "Stopped high battery Looper!")
        handler.removeCallbacks(highBatteryLooper)
    }

}