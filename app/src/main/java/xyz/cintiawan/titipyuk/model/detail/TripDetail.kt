package xyz.cintiawan.titipyuk.model.detail

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.item.CityItem

data class TripDetail(
        @SerializedName("id")
        val id: Int,

        @SerializedName("tanggal_berangkat")
        val tanggalBerangkat: Long,

        @SerializedName("tanggal_kembali")
        val tanggalKembali: Long,

        @SerializedName("rincian")
        val rincian: String,

        @SerializedName("estimasi_pengiriman")
        val estimasiPengiriman: Long,

        @SerializedName("dikirim_dari")
        val dikirimDari: String,

        @SerializedName("user")
        val user: UserDetail,

        @SerializedName("asal")
        val kotaAsal: CityItem,

        @SerializedName("tujuan")
        val kotaTujuan: CityItem
)