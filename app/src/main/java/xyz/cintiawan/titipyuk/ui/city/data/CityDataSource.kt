package xyz.cintiawan.titipyuk.ui.city.data

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
import xyz.cintiawan.titipyuk.model.item.CityItem
import xyz.cintiawan.titipyuk.util.State

class CityDataSource(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource,
        private val dao: CityDao
) : PageKeyedDataSource<Int, CityItem>() {
    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, CityItem>) {
        subscriptions.add(
                api.getCities()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { onStart() }
                        .subscribe({
                                    onSuccess(it.data)
                                    callback.onResult(it.data, null, null)
                                }, {
                                    onError(it.message.toString())
                                    setRetry(Action { loadInitial(params, callback) })
                                }
                        )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, CityItem>) { }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, CityItem>) { }

    private fun onStart() {
        idler.increment()
        updateState(State.LOADING)
    }

    private fun onSuccess(data: List<CityItem>) {
        insertDb(data)
        updateState(State.DONE)
        idler.decrement()
    }

    private fun onError(msg: String) {
        Log.e("apalah_ini_error", msg)
        updateState(State.ERROR)
        idler.decrement()
    }

    private fun insertDb(data: List<CityItem>) {
        subscriptions.add(
                Observable.fromCallable { dao.insertAll(data) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe { Log.d(this.javaClass.simpleName, "INSERTED ${data.size} FROM API TO DB") }
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