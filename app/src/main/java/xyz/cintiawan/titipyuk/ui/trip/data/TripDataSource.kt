package xyz.cintiawan.titipyuk.ui.trip.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.model.filter.TripFilter
import xyz.cintiawan.titipyuk.model.item.TripItem
import xyz.cintiawan.titipyuk.model.response.TripItemResponse
import xyz.cintiawan.titipyuk.util.State

class TripDataSource(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource,
        private val dao: TripDao,
        private val filter: TripFilter
) : PageKeyedDataSource<Int, TripItem>() {
    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null
    private lateinit var servicePoint: Single<TripItemResponse>

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, TripItem>) {
        servicePoint = if(filter.auth) api.getUserTrips(1) else api.getTrips(1)
        subscriptions.add(
                servicePoint
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { onStart() }
                        .subscribe({
                                    onSuccess(it.data)
                                    callback.onResult(it.data, null, 2)
                                }, {
                                    onError(it.message.toString())
                                    setRetry(Action { loadInitial(params, callback) })
                                    getFromDb(callback)
                                }
                        )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, TripItem>) {
        servicePoint = if(filter.auth) api.getUserTrips(params.key) else api.getTrips(params.key)
        subscriptions.add(
                servicePoint
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { onStart() }
                        .subscribe({
                                    onSuccess(it.data)
                                    callback.onResult(it.data, params.key + 1)
                                }, {
                                    onError(it.message.toString())
                                    setRetry(Action { loadAfter(params, callback) })
                                }
                        )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, TripItem>) { }

    private fun onStart() {
        idler.increment()
        updateState(State.LOADING)
    }

    private fun onSuccess() {
        updateState(State.DONE)
        idler.decrement()
    }

    private fun onSuccess(data: List<TripItem>) {
        insertDb(data)
        updateState(State.DONE)
        idler.decrement()
    }

    private fun onError(msg: String) {
        Log.d("apalah_ini_error", msg)
        updateState(State.ERROR)
        idler.decrement()
    }

    private fun insertDb(data: List<TripItem>) {
        subscriptions.add(
                Observable.fromCallable { dao.insertAll(data) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe { Log.d(this.javaClass.simpleName, "INSERTED ${data.size} FROM API TO DB") }
        )
    }

    private fun getFromDb(callback: LoadInitialCallback<Int, TripItem>) {
        subscriptions.add(
                dao.getTrips()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .doOnSubscribe { onStart() }
                        .subscribe ({ data ->
                            onSuccess()
                            callback.onResult(data, null, null)
                        }, {
                            onError(it.message.toString())
                        })
        )
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if(action == null) null else Completable.fromAction(action)
    }

    fun retry() {
        retryCompletable?.let {
            subscriptions.add(it
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe())
        }
    }

}