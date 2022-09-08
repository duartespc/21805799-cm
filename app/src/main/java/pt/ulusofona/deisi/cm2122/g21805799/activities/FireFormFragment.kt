package pt.ulusofona.deisi.cm2122.g21805799.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21805799.model.DataManager
import pt.ulusofona.deisi.cm2122.g21805799.model.Fire
import pt.ulusofona.deisi.cm2122.g21805799.R
import pt.ulusofona.deisi.cm2122.g21805799.data.FiresRepository
import pt.ulusofona.deisi.cm2122.g21805799.databinding.FragmentFireFormBinding
import pt.ulusofona.deisi.cm2122.g21805799.ui.viewModels.FiresViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet



class FireFormFragment : Fragment() {
    private lateinit var binding: FragmentFireFormBinding
    private val model: DataManager = FiresRepository.getInstance()
    lateinit var name: EditText
    lateinit var cc: EditText
    lateinit var submitButton: Button
    lateinit var reportNowButton: Button
    lateinit var builder: AlertDialog.Builder
    lateinit var district: EditText
    lateinit var districtFromApi: String
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    private lateinit var viewModel: FiresViewModel


    private var districtLocation: HashMap<String, LatLng> = HashMap()

    private var districtsPortugal: HashSet<String> = hashSetOf("aveiro","beja","braga","bragança","castelo Branco","coimbra","évora",
        "faro","guarda","leiria","lisboa","portalegre","porto","santarém","setúbal","viana do Castelo","vila Real","viseu")

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_fire_form, container, false)
        binding = FragmentFireFormBinding.bind(view)
        builder = AlertDialog.Builder(context)
        builder.setMessage(getString(R.string.dialog_message)) .setTitle(getString(R.string.dialog_title))
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        viewModel = ViewModelProvider(this).get(FiresViewModel::class.java)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        districtLocation.put("aveiro", LatLng(40.6393791,-8.6621376))
        districtLocation.put("beja", LatLng(38.0222682,-8.0321561))
        districtLocation.put("braga", LatLng(41.5472695,-8.4464407))
        districtLocation.put("faro", LatLng(37.0177187,-8.048441))
        districtLocation.put("lisboa", LatLng(38.7436057,-9.2302434))
        districtLocation.put("porto", LatLng(41.1621376,-8.6569731))

        binding.submitButton.setOnClickListener { onSubmit() }

        name = binding.name
        cc = binding.cc
        submitButton = binding.submitButton
        reportNowButton = binding.reportNowButton
        district = binding.district

        FiresRepository.getInstance().isReportaJaOn {
            if (it) {
                binding.reportNowButton?.visibility = View.VISIBLE
                binding.reportNowButton.setOnClickListener { onReportNow() }
            } else {
                binding.reportNowButton?.visibility = View.INVISIBLE

            }
        }

    }

    private fun isEmpty(text: EditText): Boolean {
        val str: CharSequence = text.text.toString()
        return TextUtils.isEmpty(str)
    }

    private fun isValidDistrict(text:EditText): Boolean {
        val str: CharSequence = text.text.toString().lowercase().trim()
        return districtsPortugal.contains(str)
    }

    private fun isValidCCNumber(text: EditText): Boolean {
        val str: CharSequence = text.text.toString()
        return (str.isDigitsOnly() && str.length == 9)
    }

    private fun onSubmit() {
        val entryCurrentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val entryCurrentTime: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        if (isEmpty(name)) {
            name.error = getString(R.string.name_error)
        }
        if (!isValidCCNumber(cc) ) {
            cc.error = getString(R.string.cc_error)
        }
        if (isEmpty(district) || !isValidDistrict(district)) {
            district.error = getString(R.string.district_error)
        }

        if (!isEmpty(name) && !isEmpty(cc) && !isEmpty(district) && isValidDistrict(district) && entryCurrentDate.isNotBlank() && entryCurrentTime.isNotBlank()) {
            var latitude: Double = 0.0; var longitude: Double = 0.0
            val location: LatLng? = districtLocation.get(district.text.toString().lowercase().trim())
            if (location != null) {
                latitude = location.latitude
                longitude = location.longitude
            }
            builder.setMessage(R.string.dialog_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes) { _dialog, _id ->
                        Toast.makeText(activity, R.string.yes_choice,
                                Toast.LENGTH_SHORT).show()
                        model.insertFire(
                            Fire("Manual", entryCurrentDate, entryCurrentTime, "", 0, 0, 0, district.text.toString(), "", "", latitude, longitude,
                                0, "Em Curso", getString(R.string.not_available), getString(R.string.not_available), true, Date().time.toInt(), Date().time.toInt(), getString(R.string.not_available), name.text.toString(), cc.text.toString())) {
                        }
                        activity?.onBackPressed()
                    }
                    .setNegativeButton(R.string.no) { dialog, _id -> //  Action for 'NO' Button
                        dialog.cancel()
                        Toast.makeText(activity, R.string.no_choice,
                                Toast.LENGTH_SHORT).show()
                    }
            val alert = builder.create()
            alert.show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun onReportNow() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        model.getDistrict(location.latitude.toString(), location.longitude.toString()) {
                            districtFromApi = it
                        }
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        //model.getDistrict()
                        if (!list.isEmpty()) {
                            val entryCurrentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                            val entryCurrentTime: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                            builder.setMessage(R.string.report_now_dialog_message)
                                .setCancelable(false)
                                .setPositiveButton(R.string.yes) { _dialog, _id ->
                                    Toast.makeText(activity, getString(R.string.yes_choice),
                                        Toast.LENGTH_SHORT).show()
                                    model.insertFire(
                                        Fire("Manual", entryCurrentDate, entryCurrentTime, list[0].locality, -1, -1, -1, districtFromApi, context?.resources!!.getString(R.string.not_available), context?.resources!!.getString(R.string.not_available), list[0].latitude, list[0].longitude,
                                            0, context?.resources!!.getString(R.string.reported), context?.resources!!.getString(R.string.not_available), context?.resources!!.getString(R.string.not_available), true, Date().time.toInt(), Date().time.toInt(), context?.resources!!.getString(R.string.not_available), "ReportaJa", "000000000")) {
                                    }
                                    activity?.onBackPressed()
                                }
                                .setNegativeButton(R.string.no) { dialog, _id -> //  Action for 'NO' Button
                                    dialog.cancel()
                                    Toast.makeText(activity, R.string.no_choice,
                                        Toast.LENGTH_SHORT).show()
                                }
                            val alert = builder.create()
                            alert.show()
                        } else {
                            Toast.makeText(activity, R.string.location_not_found,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {
                        // LOCATION == NULL
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


}