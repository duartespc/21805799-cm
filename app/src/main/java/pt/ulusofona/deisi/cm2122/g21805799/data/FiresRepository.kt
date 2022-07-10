package pt.ulusofona.deisi.cm2122.g21805799.data

import android.content.Context
import android.util.Log
import pt.ulusofona.deisi.cm2122.g21805799.R
import pt.ulusofona.deisi.cm2122.g21805799.data.remote.ConnectivityUtil
import pt.ulusofona.deisi.cm2122.g21805799.model.Fire
import pt.ulusofona.deisi.cm2122.g21805799.model.DataManager
import java.lang.IllegalStateException

class FiresRepository private constructor(val local: DataManager, val remote: DataManager, val context: Context): DataManager() {

    override fun getAllFires(onFinished: (List<Fire>) -> Unit) {
        if (ConnectivityUtil.isOnline(context)) {
            Log.i("APP", "App is online. Getting fires from the server...")
            remote.getAllFires { fires ->
                Log.i("APP", "Got ${fires.size} fires from the server")
                local.clearAllFires {
                    Log.i("APP", "Cleared DB")
                    local.insertAllFires(fires) {
                        Log.i("APP","Inserted all fires got from WebService!")
                        local.getAllFires (onFinished)
                        //onFinished(fires) If user is on-line, this only shows the fires got from web service, not the ones in local storage
                    }
                }
            }
        } else {
            Log.i("APP", "App is offline. Getting fires from the database...")
            local.getAllFires(onFinished)
        }
    }

    override fun getLast7DaysTotal(onFinished: (String) -> Unit) {
        if (ConnectivityUtil.isOnline(context)) {
            Log.i("APP", "App is online. Getting DashBoard Info from the server...")
            remote.getLast7DaysTotal {
                onFinished(it)
            }
        } else {
            onFinished("0")
        }
    }

    override fun getActiveFiresTotal(onFinished: (String) -> Unit) {
        if (ConnectivityUtil.isOnline(context)) {
            Log.i("APP", "App is online. Getting DashBoard Info from the server...")
            remote.getActiveFiresTotal {
                onFinished(it)
            }
        } else {
            onFinished("0")
        }
    }


    override fun insertFire(fire: Fire, onFinished: () -> Unit) {
        local.insertFire(fire) {
            onFinished()
        }
    }

    override fun insertAllFires(fires: List<Fire>, onFinished: () -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun clearAllFires(onFinished: () -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun getRisk(municipality: String, onFinished: (String) -> Unit) {
        if (ConnectivityUtil.isOnline(context)) {
            Log.i("APP", "App is online. Getting risk from the server...")
            remote.getRisk(municipality) {
                onFinished(it)
            }
        } else {
            onFinished(context.resources.getString(R.string.info_not_available))
        }
    }

    companion object {
        private var instance: FiresRepository? = null

        fun init(local: DataManager, remote: DataManager, context: Context) {
            synchronized(this) {
                if (instance == null) {
                    instance = FiresRepository(local, remote, context)
                }
            }
        }

        fun getInstance(): FiresRepository {
            if (instance == null) {
                throw IllegalStateException("singleton not initialized")
            }
            return instance as FiresRepository

        }
    }
}