package pt.ulusofona.deisi.cm2122.g21805799

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import pt.ulusofona.deisi.cm2122.g21805799.databinding.FragmentFiresMapBinding
import java.text.SimpleDateFormat
import java.util.*

class FiresMapFragment : Fragment() {

    private lateinit var binding: FragmentFiresMapBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fires_map, container, false)
        binding = FragmentFiresMapBinding.bind(view)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }


}