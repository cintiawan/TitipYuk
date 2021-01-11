package xyz.cintiawan.titipyuk.model.detail

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.item.CityItem

data class PreOrderDetail(
        @SerializedName("id")
        val id: Int,

        @SerializedName("dikirim_dari")
        val dikirimDari: String,

        @SerializedName("estimasi_pengiriman")
        val estimasiPengiriman: Long,

        @SerializedName("expired")
        val expired: Long,

        @SerializedName("status")
        val status: String,

        @SerializedName("detail")
        val barang: BarangDetail,

        @SerializedName("user")
        val shopper: UserDetail,

        @SerializedName("dibelidari")
        val dibeliDari: CityItem
)