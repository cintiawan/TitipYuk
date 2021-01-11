package xyz.cintiawan.titipyuk.ui.request.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.detail.RequestDetail
import xyz.cintiawan.titipyuk.ui.request.data.RequestRepository
import xyz.cintiawan.titipyuk.util.*

class RequestDetailViewModel(
        private val repository: RequestRepository,
        private val subscriptions: CompositeDisposable,
        private val id: Int
) : BaseViewModel() {
    // Live Data
    private val requestRepositoryLiveData = MutableLiveData<RequestRepository>()
    val barangImages = MutableLiveData<List<String>>()
    val barangName = MutableLiveData<String>()
    val barangPrice = MutableLiveData<String>()
    val userImage = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val userLastOnline = MutableLiveData<String>()
    val userRating = MutableLiveData<String>()
    val userFollowers = MutableLiveData<String>()
    val userTitipan = MutableLiveData<String>()
    val requestRincian = MutableLiveData<String>()
    val flagFrom = MutableLiveData<String>()
    val barangFrom = MutableLiveData<String>()
    val flagSentFrom = MutableLiveData<String>()
    val barangSentFrom = MutableLiveData<String>()
    val kategori = MutableLiveData<String>()
    val jumlah = MutableLiveData<String>()
    val buttonVisibility = MutableLiveData<Boolean>()

    val state: LiveData<State>
    val errorMessage: LiveData<String>
    private var completable: Completable? = null

    var userUid = ""

    init {
        requestRepositoryLiveData.value = repository
        state = Transformations.switchMap<RequestRepository, State>(requestRepositoryLiveData, RequestRepository::state)
        errorMessage = Transformations.switchMap<RequestRepository, String>(requestRepositoryLiveData, RequestRepository::errorMessage)

        loadRequestDetail()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    private fun loadRequestDetail() {
        subscriptions.add(repository.getDetail(id)
                .subscribe({ response ->
                    onLoadRequestDetailSuccess(response.data[0])
                }, {
                    setRetry(Action { loadRequestDetail() })
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

    private fun onLoadRequestDetailSuccess(data: RequestDetail) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        userUid = data.penitip.uid

        barangImages.value = data.barang.gambarArray()
        barangName.value = data.barang.nama
        barangPrice.value = data.barang.varian[0].harga.toRupiahFormat()
        userImage.value = data.penitip.image
        userName.value = data.penitip.name
        userLastOnline.value = lastOnline(data.penitip.lastOnline.getPassedTime())
        userRating.value = data.penitip.rating.toString()
        userFollowers.value = data.penitip.follower.toString()
        userTitipan.value = data.penitip.titipan.toString()
        requestRincian.value = data.barang.deskripsi
        flagFrom.value = data.dibeliDari.negara.flag
        barangFrom.value = data.dibeliDari.name
        flagSentFrom.value = Constants.DEFAULT_FLAG
        barangSentFrom.value = data.dikirimKe
        kategori.value = data.barang.kategori.name
        jumlah.value = data.jumlah.toString()
        buttonVisibility.value = !currentUserUid.isNullOrEmpty() && data.penitip.uid != currentUserUid
    }

    private fun lastOnline(passed: Long): String {
        var result = "Online: "
        when {
            passed.toDays() > 0 -> result += "${passed.toDays()} Hari yang lalu"
            passed.toHours() > 0 -> result += "${passed.toHours()} Jam yang lalu"
            passed.toMinutes() > 0 -> result += "${passed.toMinutes()} Menit yang lalu"
            else -> result = "Sedang Online"
        }

        return result
    }

}