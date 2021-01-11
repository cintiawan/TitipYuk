package xyz.cintiawan.titipyuk.model.response

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.item.ReviewItem

data class ReviewItemResponse(@SerializedName("data") val data: List<ReviewItem>)