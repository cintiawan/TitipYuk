package xyz.cintiawan.titipyuk.ui.preorder.story

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.PreOrderItem

class StoryPreOrderViewModel(val obj: PreOrderItem) : BaseViewModel() {
    private var counter = 0

    // Live Data
    val id = MutableLiveData<Int>()
    val image = MutableLiveData<String>()
    val userImage = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val buttonVisibility = MutableLiveData<Boolean>()

    val moveFragment = MutableLiveData<Int>()

    init {
        id.value = obj.id
        image.value = obj.barang.gambars[counter].pathGambar
        userImage.value = obj.shopper.image
        userName.value = obj.shopper.name
    }

    fun prev() {
        if(counter - 1 < 0) {
            moveFragment.value = 0
            return
        }
        image.value = obj.barang.gambars[--counter].pathGambar
        if(counter == 0) buttonVisibility.value = true
    }

    fun next() {
        image.value = obj.barang.gambars[++counter].pathGambar
        buttonVisibility.value = false
    }

}