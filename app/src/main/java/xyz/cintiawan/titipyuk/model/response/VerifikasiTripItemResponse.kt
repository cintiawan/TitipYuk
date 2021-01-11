package xyz.cintiawan.titipyuk.model.response

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.item.VerifikasiTripItem

data class VerifikasiTripItemResponse(@SerializedName("data") val data: List<VerifikasiTripItem>)