package pt.ulusofona.deisi.cm2122.g21805799.data.remote

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


// only used for parsing JSON response
data class FireJson(val id: String, val date: String,
                    val hour: String, val location: String, val aerial: Int,
                    val man: Int, val terrain: Int, val district: String,
                    val concelho: String, val freguesia: String,
                    val lat: Double, val lng: Double, val statusCode: Int,
                    val status: String, val localidade: String, val detailLocation: String?,
                    val active: Boolean, val created: Timestamp, val updated: Timestamp)
data class Timestamp(val sec: Int)
data class Stat(val label: String, val total: Int)
data class ResourcesStat(val date: String, val total: Int, val cars: Int, val aerial: Int, val man: Int)


// only used for parsing JSON response
data class GetFiresResponse(val sucess: Boolean, val data: List<FireJson>)

data class GetRiskResponse(val sucess: Boolean, val data: String)

data class GetLast7DaysTotalResponse(val sucess: Boolean, val data: List<Stat>)

data class GetActiveFiresTotalResponse(val sucess: Boolean, val data: ResourcesStat)

interface FireService {

    @Headers("Content-Type: application/json")
    @GET("new/fires")
    suspend fun getFires(): GetFiresResponse

    @Headers("Content-Type: application/json")
    @GET("v1/risk")
    suspend fun getRisk(
        @Query("concelho") concelho: String?
    ): GetRiskResponse

    @Headers("Content-Type: application/json")
    @GET("v1/stats/week")
    suspend fun getLast7DaysTotal(): GetLast7DaysTotalResponse

    @Headers("Content-Type: application/json")
    @GET("v1/now")
    suspend fun getActiveFiresTotal(): GetActiveFiresTotalResponse

}

