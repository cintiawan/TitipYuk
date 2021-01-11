package xyz.cintiawan.titipyuk.ui.trip.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.CityItem
import xyz.cintiawan.titipyuk.model.item.CityOngkirItem
import xyz.cintiawan.titipyuk.model.parameter.TripParam
import xyz.cintiawan.titipyuk.ui.city.data.CityRepository
import xyz.cintiawan.titipyuk.ui.trip.data.TripRepository
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.dateToMillis
import xyz.cintiawan.titipyuk.util.toHtml
import java.util.*

class PostTripViewModel(
        private val tripRepository: TripRepository,
        private val cityRepository: CityRepository,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    private val cityRepositoryLiveData = MutableLiveData<CityRepository>()
    private val tripRepositoryLiveData = MutableLiveData<TripRepository>()
    val buttonEnable = MutableLiveData<Boolean>()
    val kotaText = MutableLiveData<String>()
    val kotaItem = MutableLiveData<List<CityItem>>()
    val kotaOngkirItem = MutableLiveData<List<CityOngkirItem>>()
    val keberangkatanError = MutableLiveData<String>()
    val keberangkatanText = MutableLiveData<String>()
    val keberangkatanSelection = MutableLiveData<Int>()
    val kembaliError = MutableLiveData<String>()
    val kembaliText = MutableLiveData<String>()
    val kembaliSelection = MutableLiveData<Int>()
    val pengirimanError = MutableLiveData<String>()
    val pengirimanText = MutableLiveData<String>()
    val pengirimanSelection = MutableLiveData<Int>()
    val rincianError = MutableLiveData<String>()

    private var stateKota = State.LOADING
    private var stateKotaOngkir = State.LOADING
    val stateInit = MediatorLiveData<State>()
    val errorMessageInit = MediatorLiveData<String>()
    val statePost: LiveData<State>
    val errorMessagePost: LiveData<String>
    val successMessage = MutableLiveData<String>()
    private var completable: Completable? = null

    private var kotaAsal = 0
    private var kotaAsalStr = ""
    private var kotaTujuan = 0
    private var kotaTujuanStr = ""
    private var kotaPengiriman = ""
    private var keberangkatan = ""
    private var kembali = ""
    private var pengiriman = ""
    private val tanggalEms = 8
    private var rincian = ""
    private val rincianMinEms = 10
    private val rincianMaxEms = 500

    init {
        buttonEnable.value = false

        cityRepositoryLiveData.value = cityRepository
        tripRepositoryLiveData.value = tripRepository

        stateInit.value = State.LOADING
        stateInit.addSource(Transformations.switchMap<CityRepository, State>(cityRepositoryLiveData, CityRepository::state)) {
            stateKota = it
            stateInit.value = checkState()
        }
        stateInit.addSource(Transformations.switchMap<CityRepository, State>(cityRepositoryLiveData, CityRepository::stateOngkir)) {
            stateKotaOngkir = it
            stateInit.value = checkState()
        }
        errorMessageInit.addSource(Transformations.switchMap<CityRepository, String>(cityRepositoryLiveData, CityRepository::errorMessage)) { errorMessageInit.value = it }

        statePost = Transformations.switchMap<TripRepository, State>(tripRepositoryLiveData, TripRepository::state)
        errorMessagePost = Transformations.switchMap<TripRepository, String>(tripRepositoryLiveData, TripRepository::errorMessage)

        loadInit()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    private fun loadInit() {
        subscriptions.add(cityRepository.getCities()
                .subscribe({ response ->
                    kotaItem.value = response.data
                }, {
                    setRetry(Action { loadInit() })
                })
        )
        subscriptions.add(cityRepository.getCitiesOngkir()
                .subscribe({ response ->
                    kotaOngkirItem.value = response
                }, {
                    setRetry(Action { loadInit() })
                })
        )
    }

    fun postTrip() {
        val data = TripParam(
                kotaAsal,
                kotaTujuan,
                keberangkatan.dateToMillis(),
                kembali.dateToMillis(),
                rincian,
                pengiriman.dateToMillis(),
                kotaPengiriman,
                kembali.dateToMillis()
        )
        subscriptions.add(tripRepository.post(data)
                .subscribe({
                    successMessage.value = "Sukses menambahkan Trip baru"
                }, {
                    setRetry(Action { postTrip() })
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

    fun selectKotaAsal(item: CityItem) {
        kotaAsal = item.id
        kotaAsalStr = item.name
        kotaText.value = "$kotaAsalStr ${"&#9658;".toHtml()} $kotaTujuanStr"
        checkButton()
    }

    fun selectKotaTujuan(item: CityItem) {
        kotaTujuan = item.id
        kotaTujuanStr = item.name
        kotaText.value = "$kotaAsalStr ${"&#9658;".toHtml()} $kotaTujuanStr"
        checkButton()
    }

    fun selectKotaPengiriman(item: CityOngkirItem) {
        kotaPengiriman = item.cityName
        checkButton()
    }

    fun validateKeberangkatan(text: String?) {
        if(text != keberangkatan) {
            val temp = keberangkatan
            keberangkatan = polaTanggal(text.toString())
            keberangkatanText.value = keberangkatan
            keberangkatanSelection.value = selectionTanggal(text.toString(), temp)
        }
        keberangkatanError.value = validasiTanggal(keberangkatan)
        checkButton()
    }

    fun validateKembali(text: String?) {
        if(text != kembali) {
            val temp = kembali
            kembali = polaTanggal(text.toString())
            kembaliText.value = kembali
            kembaliSelection.value = selectionTanggal(text.toString(), temp)
        }
        kembaliError.value = validasiTanggal(kembali)
        checkButton()
    }

    fun validatePengiriman(text: String?) {
        if(text != pengiriman) {
            val temp = pengiriman
            pengiriman = polaTanggal(text.toString())
            pengirimanText.value = pengiriman
            pengirimanSelection.value = selectionTanggal(text.toString(), temp)
        }
        pengirimanError.value = validasiTanggal(pengiriman)
        checkButton()
    }

    fun validateRincian(text: String?) {
        rincian = text.toString()
        when {
            text.isNullOrEmpty() -> rincianError.value = "Rincian tidak boleh kosong"
            text.length < rincianMinEms -> rincianError.value = "Minimal $rincianMinEms karakter"
            text.length > rincianMaxEms -> rincianError.value = "Maksimal $rincianMaxEms karakter"
            else -> rincianError.value = null
        }
        checkButton()
    }

    private fun validasiTanggal(text: String): String? {
        val clean = text.replace("[^\\d.]|\\.", "")
                .replace("/", "")
                .replace("D", "")
                .replace("M", "")
                .replace("Y", "")
        if(clean.length < tanggalEms || clean.length > tanggalEms) return "Format tanggal belum sesuai"

        return null
    }

    private fun polaTanggal(text: String): String {
        val cal = Calendar.getInstance()
        var clean = text.replace("[^\\d.]|\\.", "")
                .replace("/", "")
                .replace("D", "")
                .replace("M", "")
                .replace("Y", "")

        if (clean.length < 8) {
            clean += "DDMMYYYY".substring(clean.length)
        } else {
            var day = Integer.parseInt(clean.substring(0, 2))
            var mon = Integer.parseInt(clean.substring(2, 4))
            var year = Integer.parseInt(clean.substring(4, 8))

            mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
            cal.set(Calendar.MONTH, mon - 1)
            year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
            cal.set(Calendar.YEAR, year)

            day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(Calendar.DATE) else day
            clean = String.format("%02d%02d%02d", day, mon, year)
        }

        clean = String.format("%s/%s/%s",
                clean.substring(0, 2),
                clean.substring(2, 4),
                clean.substring(4, 8))

        return clean
    }

    private fun selectionTanggal(text: String, c: String): Int {
        val clean = text.replace("[^\\d.]|\\.", "")
                .replace("/", "")
                .replace("D", "")
                .replace("M", "")
                .replace("Y", "")
        val cleanC = c.replace("[^\\d.]|\\.", "")
                .replace("/", "")
                .replace("D", "")
                .replace("M", "")
                .replace("Y", "")
        var sel = clean.length
        var i = 2
        while (i <= clean.length && i < 6) {
            sel++
            i += 2
        }
        if (clean == cleanC) sel--
        sel = if(sel < 0) 0 else if (sel < text.length) sel else text.length

        return sel
    }

    private fun checkState(): State {
        return when {
            stateKota == State.ERROR || stateKotaOngkir == State.ERROR -> State.ERROR
            stateKota == State.LOADING || stateKotaOngkir == State.LOADING -> State.LOADING
            stateKota == State.DONE && stateKotaOngkir == State.DONE -> State.DONE
            else -> State.LOADING
        }
    }

    private fun checkButton() {
        buttonEnable.value = (keberangkatanError.value.isNullOrEmpty()
                && kembaliError.value.isNullOrEmpty()
                && pengirimanError.value.isNullOrEmpty()
                && rincianError.value.isNullOrEmpty()
                && kotaAsal != 0
                && kotaTujuan != 0
                && kotaPengiriman.isNotEmpty()
                && keberangkatan.isNotEmpty()
                && kembali.isNotEmpty()
                && pengiriman.isNotEmpty()
                && rincian.isNotEmpty())
    }

}