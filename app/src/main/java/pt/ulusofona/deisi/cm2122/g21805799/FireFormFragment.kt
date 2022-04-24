package pt.ulusofona.deisi.cm2122.g21805799

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import pt.ulusofona.deisi.cm2122.g21805799.databinding.FragmentFireFormBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet


class FireFormFragment : Fragment() {
    private lateinit var binding: FragmentFireFormBinding
    private val model = DataManager
    lateinit var name: EditText
    lateinit var cc: EditText
    lateinit var button: Button
    lateinit var builder: AlertDialog.Builder
    lateinit var concelho: EditText

    var concelhosPortugal: HashSet<String> = hashSetOf("Sintra","Ericeira","Mafra")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fire_form, container, false)
        binding = FragmentFireFormBinding.bind(view)
        builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.button.setOnClickListener { onSubmit() }
        name = binding.name
        cc = binding.cc
        button = binding.button
        concelho = binding.concelho
    }


    fun isEmpty(text: EditText): Boolean {
        val str: CharSequence = text.text.toString()
        return TextUtils.isEmpty(str)
    }

    fun isValidConcelho(text:EditText): Boolean {
        val str: CharSequence = text.text.toString()
        return str.isEmpty()
    }

    fun isValidCCNumber(text: EditText): Boolean {
        val str: CharSequence = text.text.toString()
        return (str.isDigitsOnly() && str.length == 8)
    }

    fun onSubmit() {
        val entryCurrentDate: String = SimpleDateFormat("dd-MM-yyyy").format(Date())
        val entryCurrentTime: String = SimpleDateFormat("HH:mm:ss").format(Date())

        if (isEmpty(name)) {
            name.setError(getString(R.string.name_error));
        }
        if (isValidCCNumber(cc) ) {
            cc.setError(getString(R.string.cc_error));
        }
        if (isEmpty(concelho)) {
            concelho.setError(getString(R.string.concelho_error));
        }

        if (!isEmpty(name) && !isEmpty(cc) && entryCurrentDate.isNotBlank() && entryCurrentTime.isNotBlank()) {
            builder.setMessage(R.string.dialog_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes) { dialog, id ->
                        Toast.makeText(activity, R.string.yes_choice,
                                Toast.LENGTH_SHORT).show()
                        model.addToFiresList(Fire("ID-TODO", entryCurrentDate, entryCurrentTime, "Sintra", "3", "4", "1", "Beja", "Ávela", "Afim", "1231", "1231",
                                "3", "Resolvido", "Freira", "Beja, Ávela, Freira", "Sim", "21/04/2022", "24/04/2022", "Nenhuma", name.text.toString(), cc.text.toString()))
                        activity?.onBackPressed()
                    }
                    .setNegativeButton(R.string.no) { dialog, id -> //  Action for 'NO' Button
                        dialog.cancel()
                        Toast.makeText(activity, R.string.no_choice,
                                Toast.LENGTH_SHORT).show()
                    }
            val alert = builder.create()
            alert.show()
        }
    }

}