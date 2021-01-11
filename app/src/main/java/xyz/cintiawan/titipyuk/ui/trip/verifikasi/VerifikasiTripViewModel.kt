package xyz.cintiawan.titipyuk.ui.trip.verifikasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.VerifikasiTripItem
import xyz.cintiawan.titipyuk.model.parameter.VerifikasiTripParam
import xyz.cintiawan.titipyuk.ui.trip.data.TripRepository
import xyz.cintiawan.titipyuk.util.State

class VerifikasiTripViewModel(
        private val repository: TripRepository,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    private val tripRepositoryLiveData = MutableLiveData<TripRepository>()
    val state: LiveData<State>
    val errorMessage: LiveData<String>

    val successMessage = MutableLiveData<String>()
    val data = MutableLiveData<List<VerifikasiTripItem>>()
    private var completable: Completable? = null

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    init {
        tripRepositoryLiveData.value = repository

        state = Transformations.switchMap<TripRepository, State>(tripRepositoryLiveData, TripRepository::state)
        errorMessage = Transformations.switchMap<TripRepository, String>(tripRepositoryLiveData, TripRepository::errorMessage)

        loadVerifikasi()
    }

    private fun loadVerifikasi() {
        subscriptions.add(
                repository.getMyOffer()
                        .subscribe({
                            data.value = it.data
                        }, {
                            setRetry(Action { loadVerifikasi() })
                        })
        )
    }

    fun respond(id: Int, respond: Int) {
        val data = VerifikasiTripParam(id, respond)
        subscriptions.add(
                repository.respondOffer(data)
                        .subscribe({
                            successMessage.value = "Sukses Menerima Request"
                        }, {
                            setRetry(Action { loadVerifikasi() })
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

    fun refresh() {
        loadVerifikasi()
    }

}