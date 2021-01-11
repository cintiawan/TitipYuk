package xyz.cintiawan.titipyuk.ui.review.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.model.item.ReviewItem
import xyz.cintiawan.titipyuk.model.filter.ReviewFilter
import xyz.cintiawan.titipyuk.model.response.ReviewItemResponse
import xyz.cintiawan.titipyuk.util.State

class ReviewDataSource(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource,
        private val filter: ReviewFilter
) : PageKeyedDataSource<Int, ReviewItem>() {
    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null
    private lateinit var servicePoint: Single<ReviewItemResponse>

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, ReviewItem>) {
        servicePoint = if(filter.auth) api.getUserReviews(1) else api.getReviews(filter.userId, 1)
        subscriptions.add(
                servicePoint
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { onStart() }
                        .subscribe({
                                    onSuccess()
                                    callback.onResult(it.data, null, 2)
                                }, {
                                    onError(it.message.toString())
                                    setRetry(Action { loadInitial(params, callback) })
                                }
                        )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ReviewItem>) {
        servicePoint = if(filter.auth) api.getUserReviews(params.key) else api.getReviews(filter.userId, params.key)
        subscriptions.add(
                servicePoint
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { onStart() }
                        .subscribe({
                                    onSuccess()
                                    callback.onResult(it.data, params.key + 1)
                                }, {
                                    onError(it.message.toString())
                                    setRetry(Action { loadAfter(params, callback) })
                                }
                        )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ReviewItem>) { }

    private fun onStart() {
        idler.increment()
        updateState(State.LOADING)
    }

    private fun onSuccess() {
        updateState(State.DONE)
        idler.decrement()
    }

    private fun onError(msg: String) {
        Log.e("apalah_ini_error", msg)
        updateState(State.ERROR)
        idler.decrement()
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