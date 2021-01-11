package xyz.cintiawan.titipyuk.ui.review.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.ReviewItem
import xyz.cintiawan.titipyuk.ui.review.data.ReviewDataSource
import xyz.cintiawan.titipyuk.ui.review.data.ReviewDataSourceFactory
import xyz.cintiawan.titipyuk.util.State

class ReviewListViewModel(
        private val dataSourceFactory: ReviewDataSourceFactory,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    val loading = MutableLiveData<Boolean>()

    lateinit var reviews: LiveData<PagedList<ReviewItem>>

    // Data Source
    private val pageSize = 10
    lateinit var state: LiveData<State>

    init {
        loadRatingListItems()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    private fun loadRatingListItems() {
        // TO BE REPLACED WITH API CALL
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build()

        reviews = LivePagedListBuilder<Int, ReviewItem>(dataSourceFactory, config).build()
        state = Transformations.switchMap<ReviewDataSource, State>(dataSourceFactory.dataSourceLiveData, ReviewDataSource::state)
    }

    fun refresh() {
        reviews.value?.dataSource?.invalidate()

        loading.value = false
    }

    fun retry() {
        dataSourceFactory.dataSourceLiveData.value?.retry()
    }

}