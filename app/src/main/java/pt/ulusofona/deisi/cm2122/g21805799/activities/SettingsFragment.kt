package pt.ulusofona.deisi.cm2122.g21805799.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pt.ulusofona.deisi.cm2122.g21805799.R
import pt.ulusofona.deisi.cm2122.g21805799.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        binding = FragmentSettingsBinding.bind(view)
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
        }
    }


}