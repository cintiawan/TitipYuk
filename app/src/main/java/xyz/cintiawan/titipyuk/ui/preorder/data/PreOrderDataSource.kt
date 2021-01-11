package xyz.cintiawan.titipyuk.ui.preorder.data

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
import xyz.cintiawan.titipyuk.model.filter.PreOrderFilter
import xyz.cintiawan.titipyuk.model.item.PreOrderItem
import xyz.cintiawan.titipyuk.model.response.PreOrderItemResponse
import xyz.cintiawan.titipyuk.util.State

class PreOrderDataSource(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource,
        private val dao: PreOrderDao,
        private val filter: PreOrderFilter
) : PageKeyedDataSource<Int, PreOrderItem>() {
    var state = MutableLiveData<State>()
    private var retryCompletable: Completable? = null
    private lateinit var servicePoint: Single<PreOrderItemResponse>

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, PreOrderItem>) {
        servicePoint = if(filter.auth) api.getUserPreOrders(1) else api.getPreOrders(1)
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PreOrderItem>) {
        servicePoint = if(filter.auth) api.getPreOrders(params.key) else api.getPreOrders(params.key)
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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PreOrderItem>) { }

    private fun onStart() {
        idler.increment()
        updateState(State.LOADING)
    }

    private fun onSuccess() {
        updateState(State.DONE)
        idler.decrement()
    }

    private fun onSuccess(data: List<PreOrderItem>) {
        insertDb(data)
        updateState(State.DONE)
        idler.decrement()
    }

    private fun onError(msg: String) {
        Log.e("apalah_ini_error", msg)
        updateState(State.ERROR)
        idler.decrement()
    }

    private fun insertDb(data: List<PreOrderItem>) {
        subscriptions.add(
                Observable.fromCallable { dao.insertAll(data) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe { Log.d(this.javaClass.simpleName, "INSERTED ${data.size} FROM API TO DB") }
        )
    }

    private fun getFromDb(callback: LoadInitialCallback<Int, PreOrderItem>) {
        subscriptions.add(
                dao.getPreOrders()
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