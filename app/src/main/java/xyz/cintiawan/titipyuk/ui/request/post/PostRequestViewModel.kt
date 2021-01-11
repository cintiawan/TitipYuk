package xyz.cintiawan.titipyuk.ui.request.post

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
import xyz.cintiawan.titipyuk.model.item.AlamatItem
import xyz.cintiawan.titipyuk.model.item.CityItem
import xyz.cintiawan.titipyuk.model.item.KategoriItem
import xyz.cintiawan.titipyuk.model.parameter.RequestParam
import xyz.cintiawan.titipyuk.ui.city.data.CityRepository
import xyz.cintiawan.titipyuk.ui.kategori.data.KategoriRepository
import xyz.cintiawan.titipyuk.ui.request.data.RequestRepository
import xyz.cintiawan.titipyuk.util.*
import java.lang.NumberFormatException
import java.util.*

class PostRequestViewModel(
        private val requestRepository: RequestRepository,
        private val kategoriRepository: KategoriRepository,
        private val cityRepository: CityRepository,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    private val requestRepositoryLiveData = MutableLiveData<RequestRepository>()
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
    val hargaError = MutableLiveData<String>()
    val hargaText = MutableLiveData<String>()
    val hargaSelection = MutableLiveData<Int>()
    val jumlahError = MutableLiveData<String>()
    val kotaItem = MutableLiveData<List<CityItem>>()
    val kotaBelumDipilih = MutableLiveData<Boolean>()
    val kotaSudahDipilih = MutableLiveData<Boolean>()
    val kotaDikirimText = MutableLiveData<String>()
    val alamatDikirimText = MutableLiveData<String>()
    val tanggalDitutupError = MutableLiveData<String>()
    val tanggalDitutupText = MutableLiveData<String>()
    val tanggalDitutupSelection = MutableLiveData<Int>()
    val deskripsiError = MutableLiveData<String>()

    private var stateKota = State.LOADING
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
    private var kategori = 0
    private var harga = 0.0
    private val hargaMinEms = 1000
    private val hargaMaxEms = 999999999999.0
    private var jumlah = 0
    private val jumlahMinEms = 1
    private val jumlahMaxEms = 99999999
    private var tanggalDitutup = ""
    private val tanggalEms = 8
    private var kotaAsal = 0
    private var kotaDikirim = 0
    private var deskripsi = ""
    private val deskripsiMinEms = 10
    private val deskripsiMaxEms = 500

    init {
        buttonEnable.value = false
        kotaSudahDipilih.value = false
        kotaBelumDipilih.value = true

        requestRepositoryLiveData.value = requestRepository
        kategoriRepositoryLiveData.value = kategoriRepository
        cityRepositoryLiveData.value = cityRepository

        stateInit.value = State.LOADING
        stateInit.addSource(Transformations.switchMap<CityRepository, State>(cityRepositoryLiveData, CityRepository::state)) {
            stateKota = it
            stateInit.value = checkState()
        }
        stateInit.addSource(Transformations.switchMap<KategoriRepository, State>(kategoriRepositoryLiveData, KategoriRepository::state)) {
            stateKategori = it
            stateInit.value = checkState()
        }
        errorMessageInit.addSource(Transformations.switchMap<CityRepository, String>(cityRepositoryLiveData, CityRepository::errorMessage)) { errorMessageInit.value = it }
        errorMessageInit.addSource(Transformations.switchMap<KategoriRepository, String>(kategoriRepositoryLiveData, KategoriRepository::errorMessage)) { errorMessageInit.value = it }

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
        subscriptions.add(kategoriRepository.getKategoris()
                .subscribe({ response ->
                    kategoriItem.value = response.data
                }, {
                    setRetry(Action { loadInit() })
                })
        )
    }

    fun postRequest() {
        val data = RequestParam(
                gambar1Path.value.toString(),
                gambar2Path.value.toString(),
                gambar3Path.value.toString(),
                gambar4Path.value.toString(),
                gambar5Path.value.toString(),
                nama,
                harga,
                deskripsi,
                kategori,
                jumlah,
                kotaAsal,
                kotaDikirim,
                tanggalDitutup.dateToMillis()
        )
        subscriptions.add(requestRepository.post(data)
                .subscribe({
                    successMessage.value = "Sukses menambahkan Request baru"
                }, {
                    setRetry(Action { postRequest() })
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

    fun validateJumlah(text: String?) {
        try { text?.let { jumlah = if(it.isNotEmpty()) it.toInt() else 0 } } catch (e: NumberFormatException) { /* More than Int.MAX_VALUE */ }
        when {
            text.isNullOrEmpty() -> jumlahError.value = "Jumlah tidak boleh kosong"
            jumlah < jumlahMinEms -> jumlahError.value = "Jumlah minimal adalah $jumlahMinEms"
            jumlah > jumlahMaxEms -> jumlahError.value = "Jumlah maximal adalah $jumlahMaxEms"
            else -> jumlahError.value = null
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

    fun selectKategori(item: KategoriItem) {
        kategori = item.id
        checkButton()
    }

    fun selectKotaAsal(item: CityItem) {
        kotaAsal = item.id
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
            stateKota == State.ERROR || stateKategori == State.ERROR -> State.ERROR
            stateKota == State.LOADING || stateKategori == State.LOADING -> State.LOADING
            stateKota == State.DONE && stateKategori == State.DONE -> State.DONE
            else -> State.LOADING
        }
    }

    fun setAlamat(data: AlamatItem) {
        kotaDikirim = data.id

        kotaSudahDipilih.value = true
        kotaBelumDipilih.value = false

        kotaDikirimText.value = data.kota
        alamatDikirimText.value = "${data.jalan}\n${data.kode_pos}"
    }

    fun checkButton() {
        buttonEnable.value = (!gambar1Path.value.isNullOrEmpty()
                && namaError.value.isNullOrEmpty()
                && hargaError.value.isNullOrEmpty()
                && jumlahError.value.isNullOrEmpty()
                && tanggalDitutupError.value.isNullOrEmpty()
                && deskripsiError.value.isNullOrEmpty()
                && nama.isNotEmpty()
                && kotaAsal != 0
                && kategori != 0
                && harga != 0.0
                && jumlah != 0
                && kotaDikirim != 0
                && tanggalDitutup.isNotEmpty()
                && deskripsi.isNotEmpty())
    }

}