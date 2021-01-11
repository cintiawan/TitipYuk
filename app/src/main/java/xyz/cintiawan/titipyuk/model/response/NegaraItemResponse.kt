package xyz.cintiawan.titipyuk.model.response

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.item.NegaraItem

data class NegaraItemResponse(@SerializedName("data") val data: List<NegaraItem>)