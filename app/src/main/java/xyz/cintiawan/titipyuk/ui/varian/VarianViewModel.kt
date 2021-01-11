package xyz.cintiawan.titipyuk.ui.varian

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.util.toHargaDoubleFormat
import xyz.cintiawan.titipyuk.util.toRupiahFormat
import xyz.cintiawan.titipyuk.util.toRupiahFormatNoSymbol
import java.lang.NumberFormatException

class VarianViewModel : BaseViewModel() {
    // Live Data
    val buttonEnable = MutableLiveData<Boolean>()
    val namaError = MutableLiveData<String>()
    val hargaError = MutableLiveData<String>()
    val hargaText = MutableLiveData<String>()
    val hargaSelection = MutableLiveData<Int>()

    var nama = ""
    private val namaMinEms = 1
    private val namaMaxEms = 50
    var harga = 0.0
    private val hargaMinEms = 1000
    private val hargaMaxEms = 999999999999.0

    fun validateNama(text: String?) {
        nama = text.toString()
        when {
            text.isNullOrEmpty() -> namaError.value = "Nama tidak boleh kosong"
            text.length < namaMinEms -> namaError.value = "Minimal $namaMinEms karakter"
            text.length > namaMaxEms -> namaError.value = "Maksimal $namaMaxEms karakter"
            else -> namaError.value = null
        }
        checkButton()
    }

    fun validateHarga(text: String?) {
        try {
            if (text.toHargaDoubleFormat() != harga) {
                harga = text.toHargaDoubleFormat()
                hargaText.value = harga.toRupiahFormatNoSymbol()
                hargaSelection.value = harga.toRupiahFormatNoSymbol().length
            } else if (text == "Rp ") {
                hargaText.value = harga.toRupiahFormatNoSymbol()
                hargaSelection.value = harga.toRupiahFormatNoSymbol().length
            }
        } catch (e: NumberFormatException) { /* More than Double.MAX_VALUE */ }
        when {
            text.isNullOrEmpty() -> hargaError.value = "Harga tidak boleh kosong"
            harga < hargaMinEms -> hargaError.value = "Harga minimal adalah ${hargaMinEms.toDouble().toRupiahFormat()}"
            harga > hargaMaxEms -> hargaError.value = "Harga maximal adalah ${hargaMaxEms.toRupiahFormat()}"
            else -> hargaError.value = null
        }
        checkButton()
    }

    private fun checkButton() {
        buttonEnable.value = (namaError.value.isNullOrEmpty()
                && hargaError.value.isNullOrEmpty()
                && nama.isNotEmpty()
                && harga > 0.0)
    }

}