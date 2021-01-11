package xyz.cintiawan.titipyuk.ui.negara.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.model.item.NegaraItem
import xyz.cintiawan.titipyuk.util.State

class NegaraDataSource(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource,
        private val dao: NegaraDao
) : PageKeyedDataSource<Int, NegaraItem>() {
    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, NegaraItem>) {
        subscriptions.add(
                api.getNegaras()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { onStart() }
                        .subscribe({
                                    onSuccess(it.data)
                                    callback.onResult(it.data, null, null)
                                }, {
                                    onError(it.message.toString())
                                    setRetry(Action { loadInitial(params, callback) })
                                    getFromDb(callback)
                                }
                        )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NegaraItem>) { }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NegaraItem>) { }

    private fun onStart() {
        idler.increment()
        updateState(State.LOADING)
    }

    private fun onSuccess() {
        updateState(State.DONE)
        idler.decrement()
    }

    private fun onSuccess(data: List<NegaraItem>) {
        insertDb(data)
        updateState(State.DONE)
        idler.decrement()
    }

    private fun onError(msg: String) {
        Log.d("apalah_ini_error", msg)
        updateState(State.ERROR)
        idler.decrement()
    }

    private fun insertDb(data: List<NegaraItem>) {
        subscriptions.add(
                Observable.fromCallable { dao.insertAll(data) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe { Log.d(this.javaClass.simpleName, "INSERTED ${data.size} FROM API TO DB") }
        )
    }

    private fun getFromDb(callback: LoadInitialCallback<Int, NegaraItem>) {
        subscriptions.add(
                dao.getNegaras()
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