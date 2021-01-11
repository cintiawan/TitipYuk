package xyz.cintiawan.titipyuk.model.item

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserItem(
        @SerializedName("id")
        val id: Int,

        @SerializedName("name")
        val name: String,

        @SerializedName("email")
        val email: String,

        @SerializedName("avatar")
        val image: String,

        @SerializedName("rating")
        val rating: Float
) : Parcelable