package xyz.cintiawan.titipyuk.model.parameter

data class TripConfirmationParam(
        val id: Int,
        val nama: String,
        val harga: Double,
        val deskripsi: String,
        val kategori: Int,
        val jumlah: Int,
        val dikirimKe: Int
)