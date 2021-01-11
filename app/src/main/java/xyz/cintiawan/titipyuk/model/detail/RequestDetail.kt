package xyz.cintiawan.titipyuk.model.detail

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.item.CityItem

data class RequestDetail (
        @SerializedName("id")
        val id: Int,

        @SerializedName("jumlah")
        val jumlah: Int,

        @SerializedName("dikirim_ke")
        val dikirimKe: String,

        @SerializedName("expired")
        val expired: Long,

        @SerializedName("status")
        val status: String,

        @SerializedName("detail")
        val barang: BarangDetail,

        @SerializedName("user")
        val penitip: UserDetail,

        @SerializedName("dibelidari")
        val dibeliDari: CityItem
)