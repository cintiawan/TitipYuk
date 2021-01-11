package xyz.cintiawan.titipyuk.ui.alamat.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.CityOngkirItem
import xyz.cintiawan.titipyuk.model.parameter.AlamatParam
import xyz.cintiawan.titipyuk.ui.city.data.CityRepository
import xyz.cintiawan.titipyuk.ui.user.data.UserRepository
import xyz.cintiawan.titipyuk.util.State

class PostAlamatViewModel(
        private val userRepository: UserRepository,
        private val cityRepository: CityRepository,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    private val userRepositoryLiveData = MutableLiveData<UserRepository>()
    private val cityRepositoryLiveData = MutableLiveData<CityRepository>()
    val buttonEnable = MutableLiveData<Boolean>()
    val jalanError = MutableLiveData<String>()
    val kodePosError = MutableLiveData<String>()
    val kotaOngkirItem = MutableLiveData<List<CityOngkirItem>>()

    val stateInit: LiveData<State>
    val errorMessageInit: LiveData<String>
    val statePost: LiveData<State>
    val errorMessagePost: LiveData<String>
    val successMessage = MutableLiveData<String>()
    private var completable: Completable? = null

    private var jalan = ""
    private val jalanMinEms = 20
    private val jalanMaxEms = 100
    private var kodePos = ""
    private val kodePosMinEms = 1
    private val kodePosMaxEms = 10
    private var kotaDikirim = ""

    init {
        buttonEnable.value = false

        userRepositoryLiveData.value = userRepository
        cityRepositoryLiveData.value = cityRepository

        stateInit = Transformations.switchMap<CityRepository, State>(cityRepositoryLiveData, CityRepository::stateOngkir)
        errorMessageInit = Transformations.switchMap<CityRepository, String>(cityRepositoryLiveData, CityRepository::errorMessage)

        statePost = Transformations.switchMap<UserRepository, State>(userRepositoryLiveData, UserRepository::state)
        errorMessagePost = Transformations.switchMap<UserRepository, String>(userRepositoryLiveData, UserRepository::errorMessage)

        loadInit()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    private fun loadInit() {
        subscriptions.add(cityRepository.getCitiesOngkir()
                .subscribe({ response ->
                    kotaOngkirItem.value = response
                }, {
                    setRetry(Action { loadInit() })
                })
        )
    }

    fun simpanAlamat() {
        val data = AlamatParam(
                kotaDikirim,
                jalan,
                kodePos
        )
        subscriptions.add(userRepository.simpanAlamat(data)
                .subscribe({
                    successMessage.value = "Sukses menambahkan alamat baru"
                }, {
                    setRetry(Action { simpanAlamat() })
                })
        )
    }

    private fun setRetry(action: Action?) {
        completable = if(action == null) null else Completable.fromAction(action)
    }

    fun retry() {
        completable?.let {
            subscriptions.add(it
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe())
        }
    }

    fun validateJalan(text: String?) {
        jalan = text.toString()
        when {
            text.isNullOrEmpty() -> jalanError.value = "Alamat Lengkap tidak boleh kosong"
            text.length < jalanMinEms -> jalanError.value = "Minimal $jalanMinEms karakter"
            text.length > jalanMaxEms -> jalanError.value = "Maksimal $jalanMaxEms karakter"
            else -> jalanError.value = null
        }
        checkButton()
    }

    fun validateKodePos(text: String?) {
        kodePos = text.toString()
        when {
            text.isNullOrEmpty() -> kodePosError.value = "Kode Pos tidak boleh kosong"
            text.length < kodePosMinEms -> kodePosError.value = "Minimal $kodePosMinEms karakter"
            text.length > kodePosMaxEms -> kodePosError.value = "Maksimal $kodePosMaxEms karakter"
            else -> kodePosError.value = null
        }
        checkButton()
    }

    fun selectKotaDikirim(item: CityOngkirItem) {
        kotaDikirim = item.cityName
        checkButton()
    }

    fun checkButton() {
        buttonEnable.value = (jalanError.value.isNullOrEmpty()
                && kodePosError.value.isNullOrEmpty()
                && jalan.isNotEmpty()
                && kodePos.isNotEmpty()
                && kotaDikirim.isNotEmpty())
    }

}