package pt.ulusofona.deisi.cm2122.g21805799

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import pt.ulusofona.deisi.cm2122.g21805799.databinding.FragmentFiresListBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FiresListFragment : Fragment() {

    private val model = DataManager
    private var adapter = FiresListAdapter(onClick = ::onFireClick)
    private lateinit var binding: FragmentFiresListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_fires_list, container, false)
        binding = FragmentFiresListBinding.bind(view)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.rvFiresList.layoutManager = LinearLayoutManager(context)
        binding.rvFiresList.adapter = adapter

        model.getFiresList { updateList(it) }

    }

    private fun onFireClick(fire: FireUi) {
        NavigationManager.goToFireDetail(parentFragmentManager, fire)
    }

    private fun updateList(fires: List<Fire>) {
        val list = fires.map { FireUi(it.id, it.date, it.hour, it.location, it.aerial, it.men, it.terrain, it.district, it.concelho, it.freguesia, it.lat, it.lng,
        it.statusCode, it.status, it.localidade, it.detailLocation, it.active, it.created, it.updated, it.observations, it.name, it.cc) }
        CoroutineScope(Dispatchers.Main).launch {
            showList(list.isNotEmpty())
            adapter.updateItems(list)
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