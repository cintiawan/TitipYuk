package xyz.cintiawan.titipyuk.model.item

import com.google.gson.annotations.SerializedName

data class KategoriItem(
        @SerializedName("id")
        val id: Int,

        @SerializedName("nama_kategori")
        val namaKategori: String
)