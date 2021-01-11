package xyz.cintiawan.titipyuk.ui.user.auth

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.parameter.AuthParam
import xyz.cintiawan.titipyuk.ui.user.data.UserRepository
import xyz.cintiawan.titipyuk.util.State

class RegisterViewModel(
        private val repository: UserRepository,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    private val userRepositoryLiveData = MutableLiveData<UserRepository>()
    val buttonEnable = MutableLiveData<Boolean>()
    val emailError = MutableLiveData<String>()
    val nameError = MutableLiveData<String>()
    val phoneNumberError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()
    val passwordConfError = MutableLiveData<String>()

    val state: LiveData<State>
    val errorMessage: LiveData<String>
    val successMessage = MutableLiveData<String>()

    private var email = ""
    private val emailMinEms = 10
    private val emailMaxEms = 100
    private var name = ""
    private val nameMinEms = 3
    private val nameMaxEms = 100
    private var phoneNumber = ""
    private val phoneNumberMinEms = 8
    private val phoneNumberMaxEms = 20
    private var password = ""
    private val passwordMinEms = 8
    private val passwordMaxEms = 100
    private var passwordConf = ""

    init {
        buttonEnable.value = false

        userRepositoryLiveData.value = repository

        state = Transformations.switchMap<UserRepository, State>(userRepositoryLiveData, UserRepository::state)
        errorMessage = Transformations.switchMap<UserRepository, String>(userRepositoryLiveData, UserRepository::errorMessage)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    fun register() {
        repository.registerEmailFirebase(AuthParam(email, "", name, phoneNumber, password, passwordConf)) {
            subscriptions.add(
                    repository.register(AuthParam(email, FirebaseAuth.getInstance().currentUser?.uid, name, phoneNumber, password, passwordConf))
                            .subscribe({
                                onRegisterSuccess(it.message)
                            }, { })
            )
        }
    }

    private fun onRegisterSuccess(msg: String) {
        successMessage.value = msg
        Log.d("message_from_api", msg)
    }

    fun validateEmail(text: String?) {
        email = text.toString()
        when {
            text.isNullOrEmpty() -> emailError.value = "Email tidak boleh kosong"
            !Patterns.EMAIL_ADDRESS.matcher(text).matches() -> emailError.value = "Format email tidak sesuai"
            text.length < emailMinEms -> emailError.value = "Minimal $emailMinEms karakter"
            text.length > emailMaxEms -> emailError.value = "Maksimal $emailMaxEms karakter"
            else -> emailError.value = null
        }
        checkButton()
    }

    fun validateName(text: String?) {
        name = text.toString()
        when {
            text.isNullOrEmpty() -> nameError.value = "Nama tidak boleh kosong"
            text.length < nameMinEms -> nameError.value = "Minimal $nameMinEms karakter"
            text.length > nameMaxEms -> nameError.value = "Maksimal $nameMaxEms karakter"
            else -> nameError.value = null
        }
        checkButton()
    }

    fun validatePhoneNumber(text: String?) {
        phoneNumber = text.toString()
        when {
            text.isNullOrEmpty() -> phoneNumberError.value = "Nomor HP tidak boleh kosong"
            text.length < phoneNumberMinEms -> phoneNumberError.value = "Minimal $phoneNumberMinEms karakter"
            text.length > phoneNumberMaxEms -> phoneNumberError.value = "Maksimal $phoneNumberMaxEms karakter"
            else -> phoneNumberError.value = null
        }
        checkButton()
    }

    fun validatePassword(text: String?) {
        password = text.toString()
        when {
            text.isNullOrEmpty() -> passwordError.value = "Password tidak boleh kosong"
            text.length < passwordMinEms -> passwordError.value = "Minimal $passwordMinEms karakter"
            text.length > passwordMaxEms -> passwordError.value = "Maksimal $passwordMaxEms karakter"
            else -> passwordError.value = null
        }
        checkButton()
    }

    fun validatePasswordConf(text: String?) {
        passwordConf = text.toString()
        when {
            text.isNullOrEmpty() -> passwordConfError.value = "Konfirmasi password tidak boleh kosong"
            text.toString() != password -> passwordConfError.value = "Password tidak sama"
            else -> passwordConfError.value = null
        }
        checkButton()
    }

    private fun checkButton() {
        buttonEnable.value = (emailError.value.isNullOrEmpty()
                && nameError.value.isNullOrEmpty()
                && phoneNumberError.value.isNullOrEmpty()
                && passwordError.value.isNullOrEmpty()
                && passwordConfError.value.isNullOrEmpty())
                && email.isNotEmpty()
                && name.isNotEmpty()
                && phoneNumber.isNotEmpty()
                && password.isNotEmpty()
                && passwordConf.isNotEmpty()
    }

}