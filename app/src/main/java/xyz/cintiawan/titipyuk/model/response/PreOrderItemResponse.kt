package xyz.cintiawan.titipyuk.model.response

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.item.PreOrderItem

data class PreOrderItemResponse(@SerializedName("data") val data: List<PreOrderItem>)