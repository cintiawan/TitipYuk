package xyz.cintiawan.titipyuk.ui.alamat.item

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.AlamatItem

class ItemAlamatViewModel : BaseViewModel() {
    val kota = MutableLiveData<String>()
    val alamat = MutableLiveData<String>()

    fun bind(data: AlamatItem?) {
        kota.value = data?.kota
        alamat.value = "${data?.jalan}\n${data?.kode_pos}"
    }
}