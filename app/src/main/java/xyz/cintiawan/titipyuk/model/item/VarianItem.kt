package xyz.cintiawan.titipyuk.model.item

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VarianItem(
        @SerializedName("id")
        val id: Int,

        @SerializedName("nama")
        val nama: String,

        @SerializedName("harga")
        val harga: Double
) : Parcelable {
    constructor() : this(0, "Pilih Varian", 0.0)

    override fun toString(): String = nama
}