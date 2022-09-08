package pt.ulusofona.deisi.cm2122.g21805799.activities

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21805799.NavigationManager
import pt.ulusofona.deisi.cm2122.g21805799.R
import pt.ulusofona.deisi.cm2122.g21805799.databinding.FragmentMapsBinding
import pt.ulusofona.deisi.cm2122.g21805799.ui.viewModels.FireUI
import pt.ulusofona.deisi.cm2122.g21805799.ui.viewModels.FiresViewModel
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt


class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var viewModel: FiresViewModel
    val fires: ArrayList<FireUI> = ArrayList()
    val firesReportedByMe: ArrayList<FireUI> = ArrayList()
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FiresViewModel::class.java)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        var lastLatitude: Double = 0.0
        var lastLongitude: Double = 0.0

        viewModel.getAllFires {
            it.map {
                mFusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        if (location != null) {
                            lastLatitude = location.latitude
                            lastLongitude = location.longitude
                        }
                        val result = FloatArray(1)
                        Location.distanceBetween(it.lat, it.lng, lastLatitude, lastLongitude, result)
                        val resultKm: Int = (result[0]/1000).roundToInt()
                        it.distanceFromMe = "$resultKm km"
                    }
                fires.add(it)
            }
        }

    }


    @SuppressLint("MissingPermission")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        binding = FragmentMapsBinding.bind(view)


        // Use this to get FireUI when clicking on a marker so it can retrieve the correct fire to be passed to fireDetailFragment
        val markerMap = HashMap<Marker, FireUI>()

        // Report fire (FAB) -> sends user to fire registering page/form
        binding.reportFireButton.setOnClickListener {
            NavigationManager.goToFireFormFragment(parentFragmentManager)
        }

        // Initialize map fragment
        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        // Add markers when Map has finished loading
        supportMapFragment!!.getMapAsync { googleMap ->

            for (fire in fires) {
                if (fire.name.contains("ReportaJa") || fire.id.contains("Manual")) {
                    firesReportedByMe.add(fire)
                    Log.i("APP", "Adding fire reported by you with name: ${fire.name} ID: ${fire.id}")
                }
                Log.i("APP", "Adding marker at lat: ${fire.lat} lng: ${fire.lng}")
                // Initialize marker options
                val markerOptions = MarkerOptions()
                markerOptions.position(LatLng(fire.lat, fire.lng))
                markerOptions.title(fire.status)
                markerOptions.snippet(fire.localidade)

                // Different type of markers according to fire's status
                when (fire.status) {
                    "Em Resolução" -> markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.fire_fight))
                    "Vigilância" -> markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.fire_out))
                    "Conclusão" -> markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.fire_out))
                    else -> markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.fire_on))
                }
                val marker = googleMap.addMarker(markerOptions)
                if (marker != null)
                    markerMap.put(marker, fire)
            }

            Toast.makeText(activity, firesReportedByMe.count().toString(),
                Toast.LENGTH_SHORT).show()

            // Zoom in on user's location
            mFusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    if (location != null) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 10f))
                    }
                }

            // Click on Marker opens Fire Detail, just like clicking on a fire on the list
            googleMap.setOnMarkerClickListener { marker ->
                markerMap.get(marker)
                    ?.let { NavigationManager.goToFireDetail(parentFragmentManager, it) }
                true
            }


        }

        return binding.root
    }



}