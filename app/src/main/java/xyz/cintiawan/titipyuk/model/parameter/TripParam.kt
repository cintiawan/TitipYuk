package xyz.cintiawan.titipyuk.model.parameter

data class TripParam(
        val kotaAsal: Int,
        val kotaTujuan: Int,
        val tanggalBerangkat: Long,
        val tanggalKembali: Long,
        val rincian: String,
        val estimasiPengiriman: Long,
        val dikirimDari: String,
        val expired: Long
)