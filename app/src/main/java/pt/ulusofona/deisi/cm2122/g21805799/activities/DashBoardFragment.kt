package pt.ulusofona.deisi.cm2122.g21805799.activities

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21805799.R
import pt.ulusofona.deisi.cm2122.g21805799.databinding.FragmentDashboardBinding
import pt.ulusofona.deisi.cm2122.g21805799.ui.viewModels.FiresViewModel


class DashBoardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var viewModel: FiresViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        view.setBackgroundColor(Color.GRAY)
        binding = FragmentDashboardBinding.bind(view)
        viewModel = ViewModelProvider(this).get(FiresViewModel::class.java)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.getDashboardInfo {
            CoroutineScope(Dispatchers.Main).launch {
                binding.activeFires.text = it[0]
                binding.firesToday.text = it[1]
                binding.firesThisWeek.text = it[2]
                binding.firesThisMonth.text = it[3]
            }
        }
    }

}