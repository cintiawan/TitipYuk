package xyz.cintiawan.titipyuk.model.parameter

data class PreOrderParam(
        val image1: String,
        val image2: String,
        val image3: String,
        val image4: String,
        val image5: String,
        val nama: String,
        val harga: String,
        val deskripsi: String,
        val berat: Int,
        val satuanBerat: String,
        val kategori: Int,
        val dibeliDari: Int,
        val dikirimDari: String,
        val estimasiPengiriman: Long,
        val expired: Long,
        val varian: String
)