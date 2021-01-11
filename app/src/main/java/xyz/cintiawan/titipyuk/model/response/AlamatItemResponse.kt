package xyz.cintiawan.titipyuk.model.response

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.item.AlamatItem

data class AlamatItemResponse(@SerializedName("data") val data: List<AlamatItem>)