package xyz.cintiawan.titipyuk.model.response

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.detail.RequestDetail

data class RequestDetailResponse(@SerializedName("data") val data: List<RequestDetail>)