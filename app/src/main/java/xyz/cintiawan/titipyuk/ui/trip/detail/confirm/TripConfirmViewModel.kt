package xyz.cintiawan.titipyuk.ui.trip.detail.confirm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.AlamatItem
import xyz.cintiawan.titipyuk.model.item.KategoriItem
import xyz.cintiawan.titipyuk.model.parameter.TripConfirmationParam
import xyz.cintiawan.titipyuk.ui.kategori.data.KategoriRepository
import xyz.cintiawan.titipyuk.ui.trip.data.TripRepository
import xyz.cintiawan.titipyuk.util.*
import java.lang.NumberFormatException

class TripConfirmViewModel(
        private val tripRepository: TripRepository,
        private val kategoriRepository: KategoriRepository,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    private val tripRepositoryLiveData = MutableLiveData<TripRepository>()
    private val kategoriRepositoryLiveData = MutableLiveData<KategoriRepository>()
    val buttonEnable = MutableLiveData<Boolean>()
    val namaError = MutableLiveData<String>()
    val kategoriItem = MutableLiveData<List<KategoriItem>>()
    val hargaError = MutableLiveData<String>()
    val hargaText = MutableLiveData<String>()
    val hargaSelection = MutableLiveData<Int>()
    val jumlahError = MutableLiveData<String>()
    val kotaBelumDipilih = MutableLiveData<Boolean>()
    val kotaSudahDipilih = MutableLiveData<Boolean>()
    val kotaDikirimText = MutableLiveData<String>()
    val alamatDikirimText = MutableLiveData<String>()
    val deskripsiError = MutableLiveData<String>()

    val stateInit: LiveData<State>
    val errorMessageInit: LiveData<String>
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
    private var kotaDikirim = 0
    private var deskripsi = ""
    private val deskripsiMinEms = 10
    private val deskripsiMaxEms = 500

    init {
        buttonEnable.value = false
        kotaSudahDipilih.value = false
        kotaBelumDipilih.value = true

        tripRepositoryLiveData.value = tripRepository
        kategoriRepositoryLiveData.value = kategoriRepository

        stateInit = Transformations.switchMap<KategoriRepository, State>(kategoriRepositoryLiveData, KategoriRepository::state)
        errorMessageInit = Transformations.switchMap<KategoriRepository, String>(kategoriRepositoryLiveData, KategoriRepository::errorMessage)

        statePost = Transformations.switchMap<TripRepository, State>(tripRepositoryLiveData, TripRepository::state)
        errorMessagePost = Transformations.switchMap<TripRepository, String>(tripRepositoryLiveData, TripRepository::errorMessage)

        loadInit()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    private fun loadInit() {
        subscriptions.add(kategoriRepository.getKategoris()
                .subscribe({ response ->
                    kategoriItem.value = response.data
                }, {
                    setRetry(Action { loadInit() })
                })
        )
    }

    fun postConfirmation(id: Int) {
        val data = TripConfirmationParam(
                id,
                nama,
                harga,
                deskripsi,
                kategori,
                jumlah,
                kotaDikirim
        )
        Log.d("apalah", data.toString())
        subscriptions.add(tripRepository.postOffer(data)
                .subscribe({
                    successMessage.value = "Sukses Mengirimkan Request"
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

    fun selectKategori(item: KategoriItem) {
        kategori = item.id
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

    fun setAlamat(data: AlamatItem) {
        kotaDikirim = data.id

        kotaSudahDipilih.value = true
        kotaBelumDipilih.value = false

        kotaDikirimText.value = data.kota
        alamatDikirimText.value = "${data.jalan}\n${data.kode_pos}"
    }

    fun checkButton() {
        buttonEnable.value = (namaError.value.isNullOrEmpty()
                && hargaError.value.isNullOrEmpty()
                && jumlahError.value.isNullOrEmpty()
                && deskripsiError.value.isNullOrEmpty()
                && nama.isNotEmpty()
                && kategori != 0
                && harga != 0.0
                && jumlah != 0
                && kotaDikirim != 0
                && deskripsi.isNotEmpty())
    }

}