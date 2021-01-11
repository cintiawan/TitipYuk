package xyz.cintiawan.titipyuk.ui.preorder.detail.confirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.VarianItem
import xyz.cintiawan.titipyuk.model.parameter.PreOrderConfirmationParam
import xyz.cintiawan.titipyuk.ui.preorder.data.PreOrderRepository
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.toRupiahFormat
import java.lang.NumberFormatException

class PreOrderConfirmViewModel(
        private val repository: PreOrderRepository,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    private val preOrderRepositoryLiveData = MutableLiveData<PreOrderRepository>()
    val buttonEnable = MutableLiveData<Boolean>()
    val jumlahError = MutableLiveData<String>()
    val catatanError = MutableLiveData<String>()
    val totalHargaText = MutableLiveData<String>()

    val statePost: LiveData<State>
    val errorMessagePost: LiveData<String>
    val successMessage = MutableLiveData<String>()
    private var completable: Completable? = null

    private var varian = 0
    private var harga = 0.0
    private var jumlah = 0
    private val jumlahMinEms = 1
    private val jumlahMaxEms = 99999999
    private var catatan = ""
    private val catatanMaxEms = 100
    private var totalHarga = 0.0

    init {
        buttonEnable.value = false

        preOrderRepositoryLiveData.value = repository

        statePost = Transformations.switchMap<PreOrderRepository, State>(preOrderRepositoryLiveData, PreOrderRepository::state)
        errorMessagePost = Transformations.switchMap<PreOrderRepository, String>(preOrderRepositoryLiveData, PreOrderRepository::errorMessage)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    fun postConfirmation(id: Int) {
        val data = PreOrderConfirmationParam(
                id,
                varian,
                jumlah
        )
        subscriptions.add(repository.postOffer(data)
                .subscribe({
                    successMessage.value = "Pesanan tersimpan di Box Titipan"
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

    fun selectVarian(item: VarianItem) {
        varian = item.id
        harga = item.harga
        checkTotal()
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
        checkTotal()
        checkButton()
    }

    fun validateCatatan(text: String?) {
        catatan = text.toString()
        when {
            !text.isNullOrEmpty() && text.length > catatanMaxEms -> catatanError.value = "Maksimal $catatanMaxEms karakter"
            else -> catatanError.value = null
        }
        checkButton()
    }

    private fun checkTotal() {
        totalHarga = harga * jumlah
        totalHargaText.value = totalHarga.toRupiahFormat()
    }

    private fun checkButton() {
        buttonEnable.value = (jumlahError.value.isNullOrEmpty()
                && varian != 0
                && jumlah != 0)
    }

}