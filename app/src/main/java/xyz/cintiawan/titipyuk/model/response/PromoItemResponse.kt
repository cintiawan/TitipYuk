package xyz.cintiawan.titipyuk.model.response

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.item.PromoItem

data class PromoItemResponse(@SerializedName("data") val data: List<PromoItem>)