package xyz.cintiawan.titipyuk.ui.kategori.item

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.KategoriItem

class ItemKategoriAutocompleteViewModel : BaseViewModel() {
    val kategoriName = MutableLiveData<String>()

    fun bind(kategori: KategoriItem?) {
        kategoriName.value = kategori?.namaKategori
    }

}