package xyz.cintiawan.titipyuk.ui.city.item

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.CityItem

class ItemCityAutocompleteViewModel : BaseViewModel() {
    val flag = MutableLiveData<String>()
    val cityName = MutableLiveData<String>()
    val cityCountry = MutableLiveData<String>()

    fun bind(city: CityItem?) {
        flag.value = city?.negara?.flag
        cityName.value = city?.name
        cityCountry.value = city?.negara?.name
    }

}