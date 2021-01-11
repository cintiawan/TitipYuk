package xyz.cintiawan.titipyuk.ui.user.auth

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.MessageLogin
import xyz.cintiawan.titipyuk.model.parameter.AuthParam
import xyz.cintiawan.titipyuk.ui.user.data.UserRepository
import xyz.cintiawan.titipyuk.util.State

class LoginViewModel(
        private val repository: UserRepository,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    private val userRepositoryLiveData = MutableLiveData<UserRepository>()
    val buttonEnable = MutableLiveData<Boolean>()
    val emailError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    val state: LiveData<State>
    val errorMessage: LiveData<String>
    val message = MutableLiveData<String>()

    private var email = ""
    private val emailMinEms = 3
    private val emailMaxEms = 100
    private var password = ""
    private val passwordMinEms = 1
    private val passwordMaxEms = 100

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

    fun login() {
        repository.loginEmailFirebase(AuthParam(email, password)) {
            subscriptions.add(
                    repository.login(AuthParam(email, password))
                            .subscribe({
                                onLoginSuccess(it)
                            }, { })
            )
        }
    }

    fun googleLogin(account: GoogleSignInAccount?) {
        repository.loginGoogleFirebase(account) {

        }
    }

    private fun onLoginSuccess(data: MessageLogin) {
        message.value = data.accessToken
        Log.d("message_from_api", data.tokenType)
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

    private fun checkButton() {
        buttonEnable.value = emailError.value.isNullOrEmpty() && passwordError.value.isNullOrEmpty() && email.isNotEmpty() && password.isNotEmpty()
    }

    fun reset() {
        buttonEnable.value = false
        emailError.value = ""
        passwordError.value = ""

        message.value = ""
    }

}