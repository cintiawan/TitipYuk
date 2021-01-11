package xyz.cintiawan.titipyuk.model.detail

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.item.ReviewItem

data class UserDetail(
        @SerializedName("id")
        val id: Int,

        @SerializedName("uid")
        val uid: String,

        @SerializedName("name")
        val name: String,

        @SerializedName("email")
        val email: String,

        @SerializedName("avatar")
        val image: String,

        @SerializedName("desription")
        val description: String,

        @SerializedName("last_online")
        val lastOnline: Long,

        @SerializedName("rating")
        val rating: Float,

        @SerializedName("follower")
        val follower: Long,

        @SerializedName("following")
        val following: Long,

        @SerializedName("titipan")
        val titipan: Long,

        @SerializedName("review")
        val review: List<ReviewItem>
)