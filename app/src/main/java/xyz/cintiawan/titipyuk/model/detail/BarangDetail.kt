package xyz.cintiawan.titipyuk.model.detail

import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.Gambar
import xyz.cintiawan.titipyuk.model.Kategori
import xyz.cintiawan.titipyuk.model.item.VarianItem

data class BarangDetail(
        @SerializedName("id")
        val id: Int,

        @SerializedName("nama")
        val nama: String,

        @SerializedName("deskripsi")
        val deskripsi: String,

        @SerializedName("berat")
        val berat: Float,

        @SerializedName("satuan_berat")
        val satuanBerat: String,

        @SerializedName("tipe")
        val tipe: Int,

        @SerializedName("varian")
        val varian: List<VarianItem>,

        @SerializedName("kategori")
        val kategori: Kategori,

        @SerializedName("gambar")
        val gambars: List<Gambar>
) {
    fun gambarArray(): List<String> {
        val result = mutableListOf<String>()
        gambars.forEach { result.add(it.pathGambar) }

        return result
    }
}