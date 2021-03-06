package xyz.cintiawan.titipyuk.ui.preorder.item

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.PreOrderItem
import xyz.cintiawan.titipyuk.util.toRupiahFormat

class ItemPreOrderViewModel : BaseViewModel() {
    // Live Data
    private val barangId = MutableLiveData<Int>()
    val barangName = MutableLiveData<String>()
    val barangPrice = MutableLiveData<String>()
    val barangFrom = MutableLiveData<String>()
    val barangImage = MutableLiveData<String>()
    val flagImage = MutableLiveData<String>()
    val barangJumlah = MutableLiveData<String>()
    val status = MutableLiveData<String>()

    fun bind(barang: PreOrderItem?) {
        barangId.value = barang?.id
        barangImage.value = barang?.barang?.gambars?.get(0)?.pathGambar
        barangName.value = barang?.barang?.nama
        barangPrice.value = barang?.barang?.varian?.get(0)?.harga.toRupiahFormat()
        barangFrom.value = barang?.dibeliDari?.name
        flagImage.value = barang?.dibeliDari?.negara?.flag
    }

}