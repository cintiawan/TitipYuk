package xyz.cintiawan.titipyuk.model.response

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.item.RequestItem

data class RequestItemResponse(@SerializedName("data") val data: List<RequestItem>)