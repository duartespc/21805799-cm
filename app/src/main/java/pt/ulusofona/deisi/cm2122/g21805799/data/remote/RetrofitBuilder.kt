package pt.ulusofona.deisi.cm2122.g21805799.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.internal.http2.Http2Connection
import java.util.concurrent.TimeUnit


object RetrofitBuilder {

    fun getInstance(baseUrl: String): Retrofit {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .callTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())

        builder.client(httpClient.build())

        val retrofit = builder.build()

        return retrofit
    }
}