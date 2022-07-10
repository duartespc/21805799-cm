package pt.ulusofona.deisi.cm2122.g21805799.ui.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.maps.model.LatLng
import pt.ulusofona.deisi.cm2122.g21805799.data.FiresRepository
import pt.ulusofona.deisi.cm2122.g21805799.model.DataManager

class FiresViewModel(application: Application): AndroidViewModel(application) {

    private val model: DataManager = FiresRepository.getInstance()

    fun getDashboardInfo(onFinished: (Array<String>) -> Unit) {
        // Gets fires an ArrayList so they can be added to the DashBoard
        // index[0] - active fires
        // index[1] - fires today
        // index[2] - fires this week
        // index[3] - fires this month
        var responseArray = Array(4) { "0" }
        model.getActiveFiresTotal {
            responseArray.set(0, it)
            model.getLast7DaysTotal {
                responseArray.set(2, it)
                model.getAllFires {
                    responseArray.set(1, it.size.toString())
                    onFinished(responseArray)
                }
            }
        }
    }


    fun getAllFires(onFinished: (ArrayList<FireUI>) -> Unit) {
        // transforms "pure" Fire into parcelable FireUI so it can be displayed on RecyclerView List
        model.getAllFires {
            Log.i("APP", "Received ${it.size} fires from WS")
            val firesUI = ArrayList(it.map { fire ->
                FireUI(
                    fire.id,
                    fire.date,
                    fire.hour,
                    fire.location,
                    fire.aerial,
                    fire.man,
                    fire.terrain,
                    fire.district,
                    fire.concelho,
                    fire.freguesia,
                    fire.lat,
                    fire.lng,
                    fire.statusCode,
                    fire.status,
                    fire.localidade,
                    fire.detailLocation,
                    fire.active,
                    fire.created,
                    fire.updated,
                    fire.observations,
                    fire.name,
                    fire.cc,
                    ""
                )
            })
            onFinished(firesUI)
        }
    }
}