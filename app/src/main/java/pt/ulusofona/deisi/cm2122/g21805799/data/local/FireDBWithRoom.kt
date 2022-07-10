package pt.ulusofona.deisi.cm2122.g21805799.data.local

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21805799.R
import pt.ulusofona.deisi.cm2122.g21805799.data.local.dao.FireOperations
import pt.ulusofona.deisi.cm2122.g21805799.data.local.entities.FireDB
import pt.ulusofona.deisi.cm2122.g21805799.model.DataManager
import pt.ulusofona.deisi.cm2122.g21805799.model.Fire


class FireDBWithRoom(private val storage: FireOperations, val context: Context): DataManager() {

    override fun insertAllFires(fires: List<Fire>, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            fires.map {
                FireDB(
                    fireId = it.id,
                    date = it.date,
                    hour = it.hour,
                    location = it.location,
                    aerial = it.aerial,
                    man = it.man,
                    terrain = it.terrain,
                    district = it.district,
                    concelho = it.concelho,
                    freguesia = it.freguesia,
                    lat = it.lat,
                    lng = it.lng,
                    statusCode = it.statusCode,
                    status = it.status,
                    localidade = it.localidade,
                    detailLocation = it.detailLocation,
                    active = it.active,
                    created = it.created,
                    updated = it.updated,
                    observations = it.observations,
                    name = it.name,
                    cc = it.cc
                )
            }.forEach {
                storage.insert(it)
                Log.i("APP", "Inserted Fire ID:${it.fireId} with fire name: ${it.name} in DB")
            }
            onFinished()
        }

    }

    override fun insertFire(fire: Fire, onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val fireDb = FireDB(
                fireId = fire.id,
                date = fire.date,
                hour = fire.hour,
                location = fire.location,
                aerial = fire.aerial,
                man = fire.man,
                terrain = fire.terrain,
                district = fire.district,
                concelho = fire.concelho,
                freguesia = fire.freguesia,
                lat = fire.lat,
                lng = fire.lng,
                statusCode = fire.statusCode,
                status = fire.status,
                localidade = fire.localidade,
                detailLocation = fire.detailLocation,
                active = fire.active,
                created = fire.created,
                updated = fire.updated,
                observations = fire.observations.toString(),
                name = fire.name.toString(),
                cc = fire.cc.toString()
            )
            storage.insert(fireDb)
            Log.i("APP", "Manually Inserted Fire name:${fire.name} in DB")
            onFinished()
        }
    }

    override fun getAllFires(onFinished: (List<Fire>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val fires = storage.getAll().map {
                Fire(
                    id = it.fireId,
                    date = it.date,
                    hour = it.hour,
                    location = it.location,
                    aerial = it.aerial,
                    man = it.man,
                    terrain = it.terrain,
                    district = it.district,
                    concelho = it.concelho,
                    freguesia = it.freguesia,
                    lat = it.lat,
                    lng = it.lng,
                    statusCode = it.statusCode,
                    status = it.status,
                    localidade = it.localidade,
                    detailLocation = it.detailLocation,
                    active = it.active,
                    created = it.created,
                    updated = it.updated,
                    observations = it.observations,
                    name = it.name,
                    cc = it.cc
                )
            }

            onFinished(fires)
        }
    }

    override fun getLast7DaysTotal(onFinished: (String) -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun getActiveFiresTotal( onFinished: (String) -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun getRisk(municipality: String, onFinished: (String) -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun clearAllFires(onFinished: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            storage.deleteAll(context.resources.getString(R.string.not_available))
            onFinished()
        }
    }


}


