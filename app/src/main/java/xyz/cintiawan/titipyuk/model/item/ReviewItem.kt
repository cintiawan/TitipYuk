package xyz.cintiawan.titipyuk.model.item

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.item.UserItem

data class ReviewItem(
        @SerializedName("user_id")
        val id: Int,

        @SerializedName("pesan")
        val pesan: String,

        @SerializedName("rating")
        val rating: Int,

        @SerializedName("reviewer")
        val reviewer: UserItem
)