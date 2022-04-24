package pt.ulusofona.deisi.cm2122.g21805799

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder
import pt.ulusofona.deisi.cm2122.g21805799.databinding.FragmentDashboardBinding

class DashBoardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private val model = DataManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = "${getString(R.string.risk)} ${getString(R.string.risk_for_this_concelho)}"
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        view.setBackgroundColor(Color.GRAY);

        binding = FragmentDashboardBinding.bind(view)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.activeFires.text = model.getActiveFires()
        binding.firesToday.text = model.getFiresToday()
        binding.firesThisWeek.text = model.getFiresThisWeek()
        binding.firesThisMonth.text = model.getFiresThisMonth()
    }

}