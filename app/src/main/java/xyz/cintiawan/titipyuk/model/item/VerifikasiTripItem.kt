package xyz.cintiawan.titipyuk.model.item

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.detail.UserDetail
import xyz.cintiawan.titipyuk.model.detail.TripDetail
import xyz.cintiawan.titipyuk.model.detail.VarianDetail

data class VerifikasiTripItem(
        @SerializedName("id")
        val id: Int,

        @SerializedName("status")
        val status: String,

        @SerializedName("jumlah")
        val jumlah: Int,

        @SerializedName("varian")
        val varian: VarianDetail,

        @SerializedName("trip")
        val trip: TripDetail,

        @SerializedName("dikirimke")
        val dikirimKe: AlamatItem,

        @SerializedName("user")
        val user: UserDetail
)