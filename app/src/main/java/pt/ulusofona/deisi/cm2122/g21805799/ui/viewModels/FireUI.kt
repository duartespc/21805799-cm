package pt.ulusofona.deisi.cm2122.g21805799.ui.viewModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import pt.ulusofona.deisi.cm2122.g21805799.R

@Parcelize
data class FireUI(
    val id: String,
    val date: String,
    val hour: String,
    val location: String,
    val aerial: Int,
    val man: Int,
    val terrain: Int,
    val district: String,
    val concelho: String,
    val freguesia: String,
    val lat: Double,
    val lng: Double,
    val statusCode: Int,
    val status: String,
    val localidade: String,
    val detailLocation: String?,
    val active: Boolean,
    val created: Int,
    val updated: Int,
    val observations: String,
    val name: String,
    val cc: String,
    var distanceFromMe : String
) : Parcelable
