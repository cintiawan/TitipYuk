package xyz.cintiawan.titipyuk.ui.city.item

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.CityOngkirItem

class ItemCityOngkirAutocompleteViewModel : BaseViewModel() {
    val cityName = MutableLiveData<String>()
    val province = MutableLiveData<String>()

    fun bind(city: CityOngkirItem?) {
        cityName.value = city?.cityName + " (${city?.type})"
        province.value = city?.province
    }

}