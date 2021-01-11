package xyz.cintiawan.titipyuk.ui.titipan.item

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.Titipan
import xyz.cintiawan.titipyuk.util.toRupiahFormat

class ItemTitipanViewModel : BaseViewModel() {
    val userImage = MutableLiveData<String>()
    val grandTotal = MutableLiveData<String>()
    val barangSentFrom = MutableLiveData<String>()
    val status = MutableLiveData<String>()
    val createdAt = MutableLiveData<String>()

    fun bind(titipan: Titipan?) {
        userImage.value = titipan?.shopper?.image
        grandTotal.value = titipan?.grandTotal.toRupiahFormat()
        barangSentFrom.value = titipan?.sentFrom?.name
        status.value = titipan?.status
    }

}