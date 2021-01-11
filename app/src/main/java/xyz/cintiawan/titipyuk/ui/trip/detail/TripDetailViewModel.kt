package xyz.cintiawan.titipyuk.ui.trip.detail

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
import xyz.cintiawan.titipyuk.model.item.ReviewItem
import xyz.cintiawan.titipyuk.model.detail.TripDetail
import xyz.cintiawan.titipyuk.ui.trip.data.TripRepository
import xyz.cintiawan.titipyuk.util.*

class TripDetailViewModel(
        private val repository: TripRepository,
        private val subscriptions: CompositeDisposable,
        private val id: Int
) : BaseViewModel() {
    // Live Data
    private val tripRepositoryLiveData = MutableLiveData<TripRepository>()
    val userImage = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val userLastOnline = MutableLiveData<String>()
    val userRating = MutableLiveData<String>()
    val userFollowers = MutableLiveData<String>()
    val userTitipan = MutableLiveData<String>()
    val from = MutableLiveData<String>()
    val flagFrom = MutableLiveData<String>()
    val to = MutableLiveData<String>()
    val flagTo = MutableLiveData<String>()
    val dateDepart = MutableLiveData<String>()
    val dateReturn = MutableLiveData<String>()
    val dateSent = MutableLiveData<String>()
    val tripRincian = MutableLiveData<String>()
    val reviews = MutableLiveData<List<ReviewItem>>()
    val buttonVisibility = MutableLiveData<Boolean>()

    val state: LiveData<State>
    val errorMessage: LiveData<String>
    private var completable: Completable? = null

    var userIdParam = 0
    var userNameParam = ""
    var userUid = ""

    init {
        tripRepositoryLiveData.value = repository
        state = Transformations.switchMap<TripRepository, State>(tripRepositoryLiveData, TripRepository::state)
        errorMessage = Transformations.switchMap<TripRepository, String>(tripRepositoryLiveData, TripRepository::errorMessage)

        loadTripDetail()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    private fun loadTripDetail() {
        subscriptions.add(repository.getDetail(id)
                .subscribe ({ response ->
                    onLoadTripDetailSuccess(response.data[0])
                }, {
                    setRetry(Action { loadTripDetail() })
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

    private fun onLoadTripDetailSuccess(data: TripDetail) {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        userIdParam = data.user.id
        userNameParam = data.user.name
        userUid = data.user.uid

        userImage.value = data.user.image
        userName.value = data.user.name
        userLastOnline.value = lastOnline(data.user.lastOnline)
        userRating.value = data.user.rating.toString()
        userFollowers.value = data.user.follower.toString()
        userTitipan.value = data.user.titipan.toString()
        from.value = data.kotaAsal.name
        flagFrom.value = data.kotaAsal.negara.flag
        to.value = data.kotaTujuan.name
        flagTo.value = data.kotaTujuan.negara.flag
        dateDepart.value = data.tanggalBerangkat.millisToDate()
        dateReturn.value = data.tanggalKembali.millisToDate()
        dateSent.value = data.estimasiPengiriman.millisToDate()
        tripRincian.value = data.rincian
        reviews.value = data.user.review
        buttonVisibility.value = !currentUserUid.isNullOrEmpty() && data.user.uid != currentUserUid
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