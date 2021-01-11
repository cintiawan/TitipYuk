package xyz.cintiawan.titipyuk.ui.user.profile.setting

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel

class SettingPasswordViewModel : BaseViewModel() {
    val buttonEnable = MutableLiveData<Boolean>()
    val passwordError = MutableLiveData<String>()
    val passwordConfError = MutableLiveData<String>()

    private var password = ""
    private val passwordMinEms = 8
    private val passwordMaxEms = 100
    private var passwordConf = ""

    // Listener
//    val passwordTextChanged = TextViewBindingAdapter.AfterTextChanged { validatePassword(it.toString()) }
//    val passwordConfTextChanged = TextViewBindingAdapter.AfterTextChanged { validatePasswordConf(it.toString()) }
//    val confirmClickListener = View.OnClickListener {  }

    init {
        buttonEnable.value = false
    }

    private fun validatePassword(text: String?) {
        password = text.toString()
        when {
            text.isNullOrEmpty() -> passwordError.value = "Password baru tidak boleh kosong"
            text.length < passwordMinEms -> passwordError.value = "Minimal $passwordMinEms karakter"
            text.length > passwordMaxEms -> passwordError.value = "Maksimal $passwordMaxEms karakter"
            else -> passwordError.value = null
        }
        checkButton()
    }

    private fun validatePasswordConf(text: String?) {
        passwordConf = text.toString()
        when {
            text.isNullOrEmpty() -> passwordConfError.value = "Konfirmasi password baru tidak boleh kosong"
            text.toString() != password -> passwordConfError.value = "Password tidak sama"
            else -> passwordConfError.value = null
        }
        checkButton()
    }

    private fun checkButton() {
        buttonEnable.value = (passwordError.value.isNullOrEmpty()
                && passwordConfError.value.isNullOrEmpty())
                && password.isNotEmpty()
                && passwordConf.isNotEmpty()
    }

}