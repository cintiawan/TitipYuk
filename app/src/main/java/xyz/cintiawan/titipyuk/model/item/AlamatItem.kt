package xyz.cintiawan.titipyuk.model.item

import com.google.gson.annotations.SerializedName

data class AlamatItem(
        @SerializedName("id")
        val id: Int,

        @SerializedName("kota_rajaongkir")
        val kota: String,

        @SerializedName("jalan")
        val jalan: String,

        @SerializedName("kode_pos")
        val kode_pos: String
)