package pt.ulusofona.deisi.cm2122.g21805799.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pt.ulusofona.deisi.cm2122.g21805799.R
import pt.ulusofona.deisi.cm2122.g21805799.databinding.FragmentFireDetailBinding
import pt.ulusofona.deisi.cm2122.g21805799.ui.viewModels.FireUI

private const val ARG_FIRE = "ARG_FIRE"

class FireDetailFragment : Fragment() {
    private var fire: FireUI? = null
    private lateinit var binding: FragmentFireDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { fire = it.getParcelable(ARG_FIRE) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_fire_detail, container, false)
        binding = FragmentFireDetailBinding.bind(view)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        fire?.let {
            binding.district.text = it.district
            binding.concelho.text = it.concelho
            binding.freguesia.text = it.freguesia
            binding.state.text = it.status
            binding.date.text = it.date
            binding.hour.text = it.hour
            binding.distance.text = it.distanceFromMe
            binding.observations.text = it.observations
            binding.name.text = it.name
            binding.cc.text = it.cc
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(fire: FireUI) =
            FireDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FIRE, fire)
                }
            }
    }
}