package xyz.cintiawan.titipyuk.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Kategori(
        @SerializedName("id")
        val id: Int,

        @SerializedName("nama_kategori")
        val name: String
) : Parcelable