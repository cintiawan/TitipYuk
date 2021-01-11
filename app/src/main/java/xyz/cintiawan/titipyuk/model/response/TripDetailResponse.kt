package xyz.cintiawan.titipyuk.model.response

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.detail.TripDetail

data class TripDetailResponse(@SerializedName("data") val data: List<TripDetail>)