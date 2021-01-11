package xyz.cintiawan.titipyuk.ui.city.item

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.CityItem

class ItemCityViewModel : BaseViewModel() {
    val cityImage = MutableLiveData<String>()
    val cityName = MutableLiveData<String>()

    fun bind(city: CityItem?) {
        cityImage.value = city?.negara?.image
        cityName.value = city?.name
    }
}