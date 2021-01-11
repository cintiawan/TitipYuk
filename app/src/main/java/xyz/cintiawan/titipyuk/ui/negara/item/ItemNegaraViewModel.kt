package xyz.cintiawan.titipyuk.ui.negara.item

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.NegaraItem

class ItemNegaraViewModel : BaseViewModel() {
    val negaraImage = MutableLiveData<String>()
    val negaraName = MutableLiveData<String>()

    fun bind(negara: NegaraItem?) {
        negaraImage.value = negara?.image
        negaraName.value = negara?.name
    }

}