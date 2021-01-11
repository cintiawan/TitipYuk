package xyz.cintiawan.titipyuk.ui.user.profile.setting

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel

class SettingProfileViewModel : BaseViewModel() {
    val buttonEnable = MutableLiveData<Boolean>()
    val nameError = MutableLiveData<String>()
    val phoneNumberError = MutableLiveData<String>()
    val addressError = MutableLiveData<String>()
    val descriptionError = MutableLiveData<String>()

    // Listener
//    val nameTextChanged = TextViewBindingAdapter.AfterTextChanged { validateName(it.toString()) }
//    val phoneNumberTextChanged = TextViewBindingAdapter.AfterTextChanged { validatePhoneNumber(it.toString()) }
//    val addressTextChanged = TextViewBindingAdapter.AfterTextChanged { validateAddress(it.toString()) }
//    val descriptionTextChanged = TextViewBindingAdapter.AfterTextChanged { validateDescription(it.toString()) }
//    val confirmClickListener = View.OnClickListener {  }

    private var name = ""
    private val nameMinEms = 3
    private val nameMaxEms = 100
    private var phoneNumber = ""
    private val phoneNumberMinEms = 8
    private val phoneNumberMaxEms = 20
    private var address = ""
    private var description = ""

    init {
        buttonEnable.value = false
    }

    private fun validateName(text: String?) {
        name = text.toString()
        when {
            text.isNullOrEmpty() -> nameError.value = "Nama tidak boleh kosong"
            text.length < nameMinEms -> nameError.value = "Minimal $nameMinEms karakter"
            text.length > nameMaxEms -> nameError.value = "Maksimal $nameMaxEms karakter"
            else -> nameError.value = null
        }
        checkButton()
    }

    private fun validatePhoneNumber(text: String?) {
        phoneNumber = text.toString()
        when {
            text.isNullOrEmpty() -> phoneNumberError.value = "Nomor HP tidak boleh kosong"
            text.length < phoneNumberMinEms -> phoneNumberError.value = "Minimal $phoneNumberMinEms karakter"
            text.length > phoneNumberMaxEms -> phoneNumberError.value = "Maksimal $phoneNumberMaxEms karakter"
            else -> phoneNumberError.value = null
        }
        checkButton()
    }

    private fun validateAddress(text: String?) {
        address = text.toString()
    }

    private fun validateDescription(text: String?) {
        description = text.toString()
    }

    private fun checkButton() {
        buttonEnable.value = (nameError.value.isNullOrEmpty()
                && phoneNumberError.value.isNullOrEmpty()
                && addressError.value.isNullOrEmpty())
                && name.isNotEmpty()
                && phoneNumber.isNotEmpty()
                && address.isNotEmpty()
    }

}