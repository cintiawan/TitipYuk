package xyz.cintiawan.titipyuk.ui.trip.verifikasi.item

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.VerifikasiTripItem
import xyz.cintiawan.titipyuk.util.toJumlahFormat
import xyz.cintiawan.titipyuk.util.toRupiahFormat

class ItemVerifikasiTripViewModel : BaseViewModel() {
    // Live Data
    val userImage = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val namaBarang = MutableLiveData<String>()
    val dibeliDariFlag = MutableLiveData<String>()
    val dibeliDari = MutableLiveData<String>()
    val harga = MutableLiveData<String>()
    val jumlah = MutableLiveData<String>()
    val rincian = MutableLiveData<String>()

    fun bind(data: VerifikasiTripItem?) {
        userImage.value = data?.user?.image
        userName.value = data?.user?.name
        namaBarang.value = data?.varian?.barang?.nama
        dibeliDariFlag.value = data?.trip?.kotaTujuan?.negara?.flag
        dibeliDari.value = data?.trip?.kotaTujuan?.name
        harga.value = data?.varian?.harga.toRupiahFormat()
        jumlah.value = data?.jumlah?.toJumlahFormat()
        rincian.value = data?.varian?.barang?.deskripsi
    }

}