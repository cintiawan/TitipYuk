package xyz.cintiawan.titipyuk.ui.preorder.detail

import android.os.CountDownTimer
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
import xyz.cintiawan.titipyuk.model.detail.PreOrderDetail
import xyz.cintiawan.titipyuk.model.item.ReviewItem
import xyz.cintiawan.titipyuk.model.item.VarianItem
import xyz.cintiawan.titipyuk.ui.preorder.data.PreOrderRepository
import xyz.cintiawan.titipyuk.util.*

class PreOrderDetailViewModel(
        private val repository: PreOrderRepository,
        private val subscriptions: CompositeDisposable,
        private val id: Int
) : BaseViewModel() {
    // Live Data
    private val preOrderRepositoryLiveData = MutableLiveData<PreOrderRepository>()
    val barangImages = MutableLiveData<List<String>>()
    val preOrderRemaining = MutableLiveData<String>()
    val barangName = MutableLiveData<String>()
    val barangPrice = MutableLiveData<String>()
    val userImage = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val userLastOnline = MutableLiveData<String>()
    val userRating = MutableLiveData<String>()
    val userFollowers = MutableLiveData<String>()
    val userTitipan = MutableLiveData<String>()
    val preOrderRincian = MutableLiveData<String>()
    val flagFrom = MutableLiveData<String>()
    val barangFrom = MutableLiveData<String>()
    val flagSentFrom = MutableLiveData<String>()
    val barangSentFrom = MutableLiveData<String>()
    val kategori = MutableLiveData<String>()
    val berat = MutableLiveData<String>()
    val reviews = MutableLiveData<List<ReviewItem>>()
    val varians = MutableLiveData<List<VarianItem>>()
    val buttonVisibility = MutableLiveData<Boolean>()

    val state: LiveData<State>
    val errorMessage: LiveData<String>
    private var completable: Completable? = null

    var userIdParam = 0
    var userNameParam = ""
    var userUid = ""

    init {
        preOrderRepositoryLiveData.value = repository
        state = Transformations.switchMap<PreOrderRepository, State>(preOrderRepositoryLiveData, PreOrderRepository::state)
        errorMessage = Transformations.switchMap<PreOrderRepository, String>(preOrderRepositoryLiveData, PreOrderRepository::errorMessage)

        loadPreOrderDetail()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    private fun loadPreOrderDetail() {
        subscriptions.add(repository.getDetail(id)
                .subscribe({ response ->
                    onLoadPreOrderDetailSuccess(response.data[0])
                }, {
                    setRetry(Action { loadPreOrderDetail() })
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

    private fun onLoadPreOrderDetailSuccess(data: PreOrderDetail) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        userIdParam = data.shopper.id
        userNameParam = data.shopper.name
        userUid = data.shopper.uid

        startTimer(data.expired.getRemainingTime())
        barangImages.value = data.barang.gambarArray()
        barangName.value = data.barang.nama
        barangPrice.value = data.barang.varian[0].harga.toRupiahFormat()
        userImage.value = data.shopper.image
        userName.value = data.shopper.name
        userLastOnline.value = lastOnline(data.shopper.lastOnline.getPassedTime())
        userRating.value = data.shopper.rating.toString()
        userFollowers.value = data.shopper.follower.toString()
        userTitipan.value = data.shopper.titipan.toString()
        preOrderRincian.value = data.barang.deskripsi
        flagFrom.value = data.dibeliDari.negara.flag
        barangFrom.value = data.dibeliDari.name
        flagSentFrom.value = Constants.DEFAULT_FLAG
        barangSentFrom.value = data.dikirimDari
        kategori.value = data.barang.kategori.name
        berat.value = "${data.barang.berat} ${data.barang.satuanBerat}"
        reviews.value = data.shopper.review
        varians.value = data.barang.varian
        buttonVisibility.value = !currentUserUid.isNullOrEmpty() && data.shopper.uid != currentUserUid
    }

    private fun startTimer(remaining: Long) {
        object : CountDownTimer(remaining, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                preOrderRemaining.value = "${millisUntilFinished.toDays()} Hari ${(millisUntilFinished.toHours())} Jam ${millisUntilFinished.toMinutes()} Menit ${millisUntilFinished.toSeconds()} Detik"
            }
            override fun onFinish() { }
        }.start()
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