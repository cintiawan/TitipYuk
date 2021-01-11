package xyz.cintiawan.titipyuk.model.parameter

data class RequestParam(
        val image1: String,
        val image2: String,
        val image3: String,
        val image4: String,
        val image5: String,
        val nama: String,
        val harga: Double,
        val deskripsi: String,
        val kategori: Int,
        val jumlah: Int,
        val dibeliDari: Int,
        val dikirimKe: Int,
        val expired: Long
)