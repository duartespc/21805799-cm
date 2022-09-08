package pt.ulusofona.deisi.cm2122.g21805799.data.remote

import com.google.gson.Gson
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import android.content.Context
import android.content.res.Resources
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2122.g21805799.R
import pt.ulusofona.deisi.cm2122.g21805799.model.DataManager
import pt.ulusofona.deisi.cm2122.g21805799.model.Fire
import retrofit2.Retrofit


class FireServiceWithRetrofit(val retrofit: Retrofit, val context: Context): DataManager() {

    override fun getReportedByMeFires(onFinished: (String) -> Unit) {
        throw Exception("Illegal operation")
    }


    override fun getDistrict(latitude: String, longitude: String, onFinished: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val request: Request = Request.Builder()
                    .url("https://geoapi.pt/gps/$latitude,$longitude")
                    .addHeader("Accept", "application/json")
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            if (!response.isSuccessful) throw IOException("Unexpected code $response")
                            val body = response.body?.string()
                            if (body != null) {
                                val jsonObject = JSONObject(body)
                                val jsonDistrict = jsonObject["distrito"] as String
                                Log.i("APP", "distrito = ${jsonDistrict}")
                                onFinished(jsonDistrict)
                            }

                        }
                    }
                })

            } catch (e: Exception) { // TENTEI SUBSTITUIR HttpException por Exception geral
                Log.i("APP", "CATCHED HTTPEXCEPTION calling getRisk(municipality)")
                onFinished("Error")  // This should be handled with a onError() callback
            }
        }
    }

    override fun insertAllFires(fires: List<Fire>, onFinished: () -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun insertFire(fire: Fire, onFinished: () -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun clearAllFires(onFinished: () -> Unit) {
        throw Exception("Illegal operation")
    }

    override fun getActiveFiresTotal(onFinished: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val service = retrofit.create(FireService::class.java)
            try {
                val responseObj: GetActiveFiresTotalResponse = service.getActiveFiresTotal()
                onFinished("${responseObj.data.total}")
            } catch (e: Exception) { // TENTEI SUBSTITUIR HttpException por Exception geral
                onFinished("0")  // This should be handled with a onError() callback
            }
        }
    }

    override fun getLast7DaysTotal(onFinished: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val service = retrofit.create(FireService::class.java)
            try {
                val responseObj: GetLast7DaysTotalResponse = service.getLast7DaysTotal()
                var count = 0
                for (stat in responseObj.data) {
                    count += stat.total
                }
                onFinished("$count")
            } catch (e: Exception) { // TENTEI SUBSTITUIR HttpException por Exception geral
                onFinished("0")  // This should be handled with a onError() callback
            }
        }
    }

    override fun getRisk (municipality: String, onFinished: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val service = retrofit.create(FireService::class.java)
            try {
                val responseObj: GetRiskResponse = service.getRisk(municipality)
                val words = responseObj.data.split("\\p{Space}".toRegex()).toTypedArray()
                var response: String = ""
                val visited: HashSet<String> = HashSet()
                for (word in words) {
                    if (visited.contains("Hoje") && visited.contains("-")) {
                        response = word.substring(0,word.length-1)
                        break
                    }
                    visited.add(word)
                }
                Log.i("APP", "getRisk response: $response")
                onFinished(response)
            } catch (e: Exception) { // TENTEI SUBSTITUIR HttpException por Exception geral
                Log.i("APP", "CATCHED HTTPEXCEPTION calling getRisk(municipality)")
                onFinished("Error")  // This should be handled with a onError() callback
            }
        }
    }

    override fun getAllFires(onFinished: (List<Fire>) -> Unit) {
        val notAvailable = context.resources.getString(R.string.not_available)
        Log.i("APP","R.string.not_available: $notAvailable")
        CoroutineScope(Dispatchers.IO).launch {
            val service = retrofit.create(FireService::class.java)
            try {
                val responseObj: GetFiresResponse = service.getFires()
                onFinished(responseObj.data.map {
                    Fire(it.id, it.date, it.hour, it.location, it.aerial,
                        it.man, it.terrain, it.district, it.concelho, it.freguesia,
                        it.lat, it.lng, it.statusCode, it.status, it.localidade, it.detailLocation,
                        it.active, it.created.sec, it.updated.sec, notAvailable, notAvailable, notAvailable)
                })
            } catch (e: Exception) { // TENTEI SUBSTITUIR HttpException por Exception geral
                Log.i("APP", "CATCHED HTTPEXCEPTION calling getAllFires()")
                onFinished(emptyList())  // This should be handled with a onError() callback
            }
        }
    }
}