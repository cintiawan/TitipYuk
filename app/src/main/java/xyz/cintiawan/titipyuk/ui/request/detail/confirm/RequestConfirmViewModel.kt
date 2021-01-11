package xyz.cintiawan.titipyuk.ui.request.detail.confirm

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
import xyz.cintiawan.titipyuk.model.parameter.RequestConfirmationParam
import xyz.cintiawan.titipyuk.model.item.CityItem
import xyz.cintiawan.titipyuk.model.item.CityOngkirItem
import xyz.cintiawan.titipyuk.ui.city.data.CityRepository
import xyz.cintiawan.titipyuk.ui.request.data.RequestRepository
import xyz.cintiawan.titipyuk.util.*
import java.lang.NumberFormatException
import java.util.*

class RequestConfirmViewModel(
        private val requestRepository: RequestRepository,
        private val cityRepository: CityRepository,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    private val requestRepositoryLiveData = MutableLiveData<RequestRepository>()
    private val cityRepositoryLiveData = MutableLiveData<CityRepository>()
    val buttonEnable = MutableLiveData<Boolean>()
    val kotaItem = MutableLiveData<List<CityItem>>()
    val kotaOngkirItem = MutableLiveData<List<CityOngkirItem>>()
    val hargaError = MutableLiveData<String>()
    val hargaText = MutableLiveData<String>()
    val hargaSelection = MutableLiveData<Int>()
    val beratError = MutableLiveData<String>()
    val beratText = MutableLiveData<String>()
    val beratSelection = MutableLiveData<Int>()
    val tanggalPengirimanError = MutableLiveData<String>()
    val tanggalPengirimanText = MutableLiveData<String>()
    val tanggalPengirimanSelection = MutableLiveData<Int>()
    val totalHargaText = MutableLiveData<String>()

    private var stateKota = State.LOADING
    private var stateKotaOngkir = State.LOADING
    val stateInit = MediatorLiveData<State>()
    val errorMessageInit = MediatorLiveData<String>()
    val statePost: LiveData<State>
    val errorMessagePost: LiveData<String>
    val successMessage = MutableLiveData<String>()
    private var completable: Completable? = null

    private var kotaAsal = 0
    private var dikirimDari = ""
    private var harga = 0.0
    private val hargaMinEms = 1000
    private val hargaMaxEms = 999999999999.0
    private var berat = 0
    private val beratMinEms = 100
    private val beratMaxEms = 99999999
    private val satuanBerat = "g"
    private var tanggalPengiriman = ""
    private val tanggalEms = 8

    init {
        buttonEnable.value = false

        requestRepositoryLiveData.value = requestRepository
        cityRepositoryLiveData.value = cityRepository

        stateInit.value = State.LOADING
        stateInit.addSource(Transformations.switchMap<CityRepository, State>(cityRepositoryLiveData, CityRepository::state)) {
            stateKota = it
            stateInit.value = checkState()
        }
        stateInit.addSource(Transformations.switchMap<CityRepository, State>(cityRepositoryLiveData, CityRepository::stateOngkir)) {
            stateKotaOngkir = it
            stateInit.value = checkState()
        }

        statePost = Transformations.switchMap<RequestRepository, State>(requestRepositoryLiveData, RequestRepository::state)
        errorMessagePost = Transformations.switchMap<RequestRepository, String>(requestRepositoryLiveData, RequestRepository::errorMessage)

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

    fun postConfirmation(id: Int) {
        val data = RequestConfirmationParam(
                id,
                kotaAsal,
                dikirimDari
        )
        subscriptions.add(requestRepository.postOffer(data)
                .subscribe({
                    successMessage.value = "Request berhasil diterima"
                }, {
                    setRetry(Action { postConfirmation(id) })
                }))
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
        checkButton()
    }

    fun selectDikirimDari(item: CityOngkirItem) {
        dikirimDari = item.cityName
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

    fun validateBerat(text: String?) {
        try {
            if (text.toBeratFloatFormat() != berat) {
                berat = text.toBeratFloatFormat()
                beratText.value = berat.toKilogramFormat()
                beratSelection.value = berat.toString().length
            } else if (text == " g") {
                beratText.value = berat.toKilogramFormat()
                beratSelection.value = berat.toString().length
            }
        } catch (e: NumberFormatException) { /* More than Float.MAX_VALUE */ }
        when {
            text.isNullOrEmpty() -> beratError.value = "Berat tidak boleh kosong"
            berat < beratMinEms -> beratError.value = "Berat minimal adalah ${beratMinEms.toKilogramFormat()}"
            berat > beratMaxEms -> beratError.value = "Berat maximal adalah ${beratMaxEms.toKilogramFormat()}"
            else -> beratError.value = null
        }
        checkButton()
    }

    fun validateTanggalPengiriman(text: String?) {
        if(text != tanggalPengiriman) {
            val temp = tanggalPengiriman
            tanggalPengiriman = polaTanggal(text.toString())
            tanggalPengirimanText.value = tanggalPengiriman
            tanggalPengirimanSelection.value = selectionTanggal(text.toString(), temp)
        }
        tanggalPengirimanError.value = validasiTanggal(tanggalPengiriman)
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
        buttonEnable.value = (kotaAsal != 0 && dikirimDari.isNotEmpty())
    }

}