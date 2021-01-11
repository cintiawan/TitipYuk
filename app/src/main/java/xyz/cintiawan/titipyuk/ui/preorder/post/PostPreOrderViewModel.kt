package xyz.cintiawan.titipyuk.ui.preorder.post

import android.util.Log
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
import xyz.cintiawan.titipyuk.model.item.KategoriItem
import xyz.cintiawan.titipyuk.model.parameter.PreOrderParam
import xyz.cintiawan.titipyuk.ui.city.data.CityRepository
import xyz.cintiawan.titipyuk.ui.kategori.data.KategoriRepository
import xyz.cintiawan.titipyuk.ui.preorder.data.PreOrderRepository
import xyz.cintiawan.titipyuk.util.*
import java.lang.NumberFormatException
import java.util.*

class PostPreOrderViewModel(
        private val preOrderRepository: PreOrderRepository,
        private val kategoriRepository: KategoriRepository,
        private val cityRepository: CityRepository,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    private val preOrderRepositoryLiveData = MutableLiveData<PreOrderRepository>()
    private val kategoriRepositoryLiveData = MutableLiveData<KategoriRepository>()
    private val cityRepositoryLiveData = MutableLiveData<CityRepository>()
    val buttonEnable = MutableLiveData<Boolean>()
    val gambar1Path = MutableLiveData<String>()
    val gambar2Path = MutableLiveData<String>()
    val gambar3Path = MutableLiveData<String>()
    val gambar4Path = MutableLiveData<String>()
    val gambar5Path = MutableLiveData<String>()
    val namaError = MutableLiveData<String>()
    val kategoriItem = MutableLiveData<List<KategoriItem>>()
    val beratError = MutableLiveData<String>()
    val beratText = MutableLiveData<String>()
    val beratSelection = MutableLiveData<Int>()
    val varianText = MutableLiveData<String>()
    val varianBelumAda = MutableLiveData<Boolean>()
    val varianSudahAda = MutableLiveData<Boolean>()
    val kotaItem = MutableLiveData<List<CityItem>>()
    val kotaOngkirItem = MutableLiveData<List<CityOngkirItem>>()
    val tanggalPengirimanError = MutableLiveData<String>()
    val tanggalPengirimanText = MutableLiveData<String>()
    val tanggalPengirimanSelection = MutableLiveData<Int>()
    val tanggalDitutupError = MutableLiveData<String>()
    val tanggalDitutupText = MutableLiveData<String>()
    val tanggalDitutupSelection = MutableLiveData<Int>()
    val deskripsiError = MutableLiveData<String>()

    private var stateKota = State.LOADING
    private var stateKotaOngkir = State.LOADING
    private var stateKategori = State.LOADING
    val stateInit = MediatorLiveData<State>()
    val errorMessageInit = MediatorLiveData<String>()
    val statePost: LiveData<State>
    val errorMessagePost: LiveData<String>
    val successMessage = MutableLiveData<String>()
    private var completable: Completable? = null

    private var nama = ""
    private val namaMinEms = 1
    private val namaMaxEms = 50
    private var kotaAsal = 0
    private var kategori = 0
    private var harga = ""
    private var varian = ""
    private var delimeter = "~@~"
    private var berat = 0
    private val beratMinEms = 100
    private val beratMaxEms = 99999999
    private val satuanBerat = "g"
    private var kotaDikirim = ""
    private var tanggalPengiriman = ""
    private var tanggalDitutup = ""
    private val tanggalEms = 8
    private var deskripsi = ""
    private val deskripsiMinEms = 10
    private val deskripsiMaxEms = 500

    init {
        buttonEnable.value = false
        varianSudahAda.value = false
        varianBelumAda.value = true

        preOrderRepositoryLiveData.value = preOrderRepository
        kategoriRepositoryLiveData.value = kategoriRepository
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
        stateInit.addSource(Transformations.switchMap<KategoriRepository, State>(kategoriRepositoryLiveData, KategoriRepository::state)) {
            stateKategori = it
            stateInit.value = checkState()
        }
        errorMessageInit.addSource(Transformations.switchMap<CityRepository, String>(cityRepositoryLiveData, CityRepository::errorMessage)) { errorMessageInit.value = it }
        errorMessageInit.addSource(Transformations.switchMap<KategoriRepository, String>(kategoriRepositoryLiveData, KategoriRepository::errorMessage)) { errorMessageInit.value = it }

        statePost = Transformations.switchMap<PreOrderRepository, State>(preOrderRepositoryLiveData, PreOrderRepository::state)
        errorMessagePost = Transformations.switchMap<PreOrderRepository, String>(preOrderRepositoryLiveData, PreOrderRepository::errorMessage)

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
        subscriptions.add(kategoriRepository.getKategoris()
                .subscribe({ response ->
                    kategoriItem.value = response.data
                }, {
                    setRetry(Action { loadInit() })
                })
        )
    }

    fun postPreOrder() {
        val data = PreOrderParam(
                gambar1Path.value.toString(),
                gambar2Path.value.toString(),
                gambar3Path.value.toString(),
                gambar4Path.value.toString(),
                gambar5Path.value.toString(),
                nama,
                harga,
                deskripsi,
                berat,
                satuanBerat,
                kategori,
                kotaAsal,
                kotaDikirim,
                tanggalPengiriman.dateToMillis(),
                tanggalDitutup.dateToMillis(),
                varian
        )
        Log.d("apalah", data.toString())
        subscriptions.add(preOrderRepository.post(data)
                .subscribe({
                    successMessage.value = "Sukses menambahkan Request baru"
                }, {
                    setRetry(Action { postPreOrder() })
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

    fun validateTanggalDitutup(text: String?) {
        if(text != tanggalDitutup) {
            val temp = tanggalDitutup
            tanggalDitutup = polaTanggal(text.toString())
            tanggalDitutupText.value = tanggalDitutup
            tanggalDitutupSelection.value = selectionTanggal(text.toString(), temp)
        }
        tanggalDitutupError.value = validasiTanggal(tanggalDitutup)
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

    fun selectKategori(item: KategoriItem) {
        kategori = item.id
        checkButton()
    }

    fun selectKotaAsal(item: CityItem) {
        kotaAsal = item.id
        checkButton()
    }

    fun selectKotaDikirim(item: CityOngkirItem) {
        kotaDikirim = item.cityName
        checkButton()
    }

    fun validateDeskripsi(text: String?) {
        deskripsi = text.toString()
        when {
            text.isNullOrEmpty() -> deskripsiError.value = "Deskripsi tidak boleh kosong"
            text.length < deskripsiMinEms -> deskripsiError.value = "Minimal $deskripsiMinEms karakter"
            text.length > deskripsiMaxEms -> deskripsiError.value = "Maksimal $deskripsiMaxEms karakter"
            else -> deskripsiError.value = null
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
            stateKota == State.ERROR || stateKotaOngkir == State.ERROR || stateKategori == State.ERROR -> State.ERROR
            stateKota == State.LOADING || stateKotaOngkir == State.LOADING || stateKategori == State.LOADING -> State.LOADING
            stateKota == State.DONE && stateKotaOngkir == State.DONE && stateKategori == State.DONE -> State.DONE
            else -> State.LOADING
        }
    }

    fun setVarian(namaVarian: String, hargaVarian: Double) {
        varianBelumAda.value = false
        varianSudahAda.value = true

        if(varian.isNotEmpty()) varian += delimeter + namaVarian else varian = namaVarian
        if(harga.isNotEmpty()) harga += delimeter + hargaVarian else harga = hargaVarian.toString()

        if(varianText.value.isNullOrEmpty())
            varianText.value = "$namaVarian - ${hargaVarian.toRupiahFormat()}\n"
        else
            varianText.value += "$namaVarian - ${hargaVarian.toRupiahFormat()}\n"
    }

    fun checkButton() {
        buttonEnable.value = (!gambar1Path.value.isNullOrEmpty()
                && namaError.value.isNullOrEmpty()
                && beratError.value.isNullOrEmpty()
                && tanggalPengirimanError.value.isNullOrEmpty()
                && tanggalDitutupError.value.isNullOrEmpty()
                && deskripsiError.value.isNullOrEmpty()
                && nama.isNotEmpty()
                && kotaAsal != 0
                && kategori != 0
                && harga.isNotEmpty()
                && varian.isNotEmpty()
                && berat > 0.0f
                && kotaDikirim.isNotEmpty()
                && tanggalPengiriman.isNotEmpty()
                && tanggalDitutup.isNotEmpty()
                && deskripsi.isNotEmpty())
    }

}