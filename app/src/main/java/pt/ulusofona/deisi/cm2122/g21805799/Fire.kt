package pt.ulusofona.deisi.cm2122.g21805799

import java.util.*

data class Fire(
    val id: String,
    val date: String,
    val hour: String,
    val location: String,
    val aerial: String,
    val men: String,
    val terrain: String,
    val district: String,
    val concelho: String,
    val freguesia: String,
    val lat: String,
    val lng: String,
    val statusCode: String,
    val status: String,
    val localidade: String,
    val detailLocation: String,
    val active: String,
    val created: String,
    val updated: String,
    val observations: String,
    val name: String,
    val cc: String
) {

    val timestamp: Long = Date().time

}
