package xyz.cintiawan.titipyuk.ui.slider

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel

class SliderViewModel(image: String?) : BaseViewModel() {
    // Live Data
    val image = MutableLiveData<String>()

    init {
        this.image.value = image
    }
}