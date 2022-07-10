package pt.ulusofona.deisi.cm2122.g21805799

import android.app.Application
import android.util.Log
import pt.ulusofona.deisi.cm2122.g21805799.data.FiresRepository
import pt.ulusofona.deisi.cm2122.g21805799.data.local.FireDBWithRoom
import pt.ulusofona.deisi.cm2122.g21805799.data.local.FireDatabase
import pt.ulusofona.deisi.cm2122.g21805799.data.remote.FireServiceWithRetrofit
import pt.ulusofona.deisi.cm2122.g21805799.data.remote.RetrofitBuilder

class FiresApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FiresRepository.init(
            local = FireDBWithRoom(FireDatabase.getInstance(this).fireOperations(), this),
            remote = FireServiceWithRetrofit(RetrofitBuilder.getInstance(FOGOS_API_BASE_URL), this),
            context = this
        )
        Log.i("APP", "Initialized repository")
    }
}