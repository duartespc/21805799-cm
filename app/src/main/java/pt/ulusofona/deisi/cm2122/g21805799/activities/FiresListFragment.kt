package pt.ulusofona.deisi.cm2122.g21805799.activities

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21805799.*
import pt.ulusofona.deisi.cm2122.g21805799.databinding.FragmentFiresListBinding
import pt.ulusofona.deisi.cm2122.g21805799.ui.adapter.FiresListAdapter
import pt.ulusofona.deisi.cm2122.g21805799.ui.viewModels.FireUI
import pt.ulusofona.deisi.cm2122.g21805799.ui.viewModels.FiresViewModel
import kotlin.math.roundToInt


class FiresListFragment : Fragment() {

    private lateinit var viewModel: FiresViewModel
    private var adapter = FiresListAdapter(onClick = ::onFireClick)
    private lateinit var binding: FragmentFiresListBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(),R.anim.to_bottom_anim)}
    private var clicked = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_fires_list, container, false)
        binding = FragmentFiresListBinding.bind(view)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.rvFiresList.layoutManager = LinearLayoutManager(context)
        binding.rvFiresList.adapter = adapter

        binding.searchBar?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter.getSearchFilter().filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.getSearchFilter().filter(newText)
                return false
            }
        })

        binding.optionsButton?.setOnClickListener {
            onOptionsButtonClicked()
        }

        binding.reportFireButton?.setOnClickListener {
            NavigationManager.goToFireFormFragment(parentFragmentManager)
        }

        binding.ascFilterButton?.setOnClickListener {
            adapter.getSortAscendingFilter().filter("")
        }

        binding.descFilterButton?.setOnClickListener {
            adapter.getSortDescendingFilter().filter("")
        }

        viewModel = ViewModelProvider(this).get(FiresViewModel::class.java)
        viewModel.getAllFires { updateList(it) }
    }


    private fun onOptionsButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.reportFireButton?.visibility = View.VISIBLE
            binding.ascFilterButton?.visibility = View.VISIBLE
            binding.descFilterButton?.visibility = View.VISIBLE
        } else {
            binding.reportFireButton?.visibility = View.INVISIBLE
            binding.ascFilterButton?.visibility = View.INVISIBLE
            binding.ascFilterButton?.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.optionsButton?.startAnimation(rotateOpen)
            binding.reportFireButton?.startAnimation(fromBottom)
            binding.ascFilterButton?.startAnimation(fromBottom)
            binding.descFilterButton?.startAnimation(fromBottom)
        } else {
            binding.optionsButton?.startAnimation(rotateClose)
            binding.reportFireButton?.startAnimation(toBottom)
            binding.ascFilterButton?.startAnimation(toBottom)
            binding.descFilterButton?.startAnimation(toBottom)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if(!clicked) {
            binding.reportFireButton?.isClickable = true
            binding.ascFilterButton?.isClickable = true
            binding.descFilterButton?.isClickable = true
        } else {
            binding.reportFireButton?.isClickable = false
            binding.ascFilterButton?.isClickable = false
            binding.descFilterButton?.isClickable = false

        }
    }


    private fun onFireClick(fire: FireUI) {
        NavigationManager.goToFireDetail(parentFragmentManager, fire)
    }

    @SuppressLint("MissingPermission")
    private fun updateList(fires: List<FireUI>) {
        var lastLatitude: Double = 0.0
        var lastLongitude: Double = 0.0
        mFusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    lastLatitude = location.latitude
                    lastLongitude = location.longitude
                }
                val list = fires.map {
                    val result = FloatArray(1)
                    Location.distanceBetween(it.lat, it.lng, lastLatitude, lastLongitude, result)
                    val resultKm: Int = (result[0]/1000).roundToInt()
                    Log.i("APP", "Lat: ${it.lat} Lng: ${it.lng} Minha lat: $lastLatitude Minha lng: $lastLongitude Distancia: $resultKm")

                    FireUI(it.id, it.date, it.hour, it.location, it.aerial, it.man, it.terrain, it.district, it.concelho, it.freguesia, it.lat, it.lng,
                        it.statusCode, it.status, it.localidade, it.detailLocation, it.active, it.created, it.updated, it.observations, it.name, it.cc, "$resultKm") }
                CoroutineScope(Dispatchers.Main).launch {
                    showList(list.isNotEmpty())
                    adapter.updateItems(list)
                }
            }

    }

    private fun showList(show: Boolean) {
        if (show) {
            binding.rvFiresList.visibility = View.VISIBLE
            binding.textNoFiresAvailable.visibility = View.GONE
        } else {
            binding.rvFiresList.visibility = View.GONE
            binding.textNoFiresAvailable.visibility = View.VISIBLE
        }
    }

}