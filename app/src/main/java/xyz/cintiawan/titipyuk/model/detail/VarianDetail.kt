package xyz.cintiawan.titipyuk.model.detail

import com.google.gson.annotations.SerializedName

data class VarianDetail(
        @SerializedName("id")
        val id: Int,

        @SerializedName("nama")
        val nama: String,

        @SerializedName("harga")
        val harga: Double,

        @SerializedName("detail")
        val barang: BarangDetail
)