package xyz.cintiawan.titipyuk.model.response

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.item.TripItem

data class TripItemResponse(@SerializedName("data") val data: List<TripItem>)