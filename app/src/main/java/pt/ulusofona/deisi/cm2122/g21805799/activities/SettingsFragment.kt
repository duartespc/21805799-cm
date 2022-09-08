package pt.ulusofona.deisi.cm2122.g21805799.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pt.ulusofona.deisi.cm2122.g21805799.R
import pt.ulusofona.deisi.cm2122.g21805799.data.FiresRepository
import pt.ulusofona.deisi.cm2122.g21805799.databinding.FragmentSettingsBinding
import pt.ulusofona.deisi.cm2122.g21805799.ui.viewModels.FiresViewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: FiresViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        binding = FragmentSettingsBinding.bind(view)
        viewModel = ViewModelProvider(this).get(FiresViewModel::class.java)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.submitButton.setOnClickListener {
            if (binding.switchButton.isChecked) {
                (activity as MainActivity).stopLowBatteryLooper()
                (activity as MainActivity).stopHighBatteryLooper()
                (activity as MainActivity).startLowBatteryLooper()
            } else {
                (activity as MainActivity).stopLowBatteryLooper()
                (activity as MainActivity).stopHighBatteryLooper()
                (activity as MainActivity).startHighBatteryLooper()
            }
            if (binding.secondSwitchButton.isChecked) {
                // Funcao reportaJa desativada
                FiresRepository.getInstance().setReportaJaOff()
                Toast.makeText(activity, "Funcao ReportaJa foi desativado",
                    Toast.LENGTH_SHORT).show()
            } else {
                FiresRepository.getInstance().setReportaJaOn()
                Toast.makeText(activity, "Funcao ReportaJa foi ativado",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }


}