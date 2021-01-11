package xyz.cintiawan.titipyuk.ui.trip.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.TripItem
import xyz.cintiawan.titipyuk.ui.trip.data.TripDataSource
import xyz.cintiawan.titipyuk.ui.trip.data.TripDataSourceFactory
import xyz.cintiawan.titipyuk.util.State

class TripListViewModel(
        private val dataSourceFactory: TripDataSourceFactory,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    val loading = MutableLiveData<Boolean>()

    lateinit var trips: LiveData<PagedList<TripItem>>

    // Data Source
    private val pageSize = 10
    lateinit var state: LiveData<State>

    init {
        loadTripListItems()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    private fun loadTripListItems() {
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build()

        trips = LivePagedListBuilder<Int, TripItem>(dataSourceFactory, config).build()
        state = Transformations.switchMap<TripDataSource, State>(dataSourceFactory.dataSourceLiveData, TripDataSource::state)
    }

    fun refresh() {
        trips.value?.dataSource?.invalidate()

        loading.value = false
    }

    fun retry() {
        dataSourceFactory.dataSourceLiveData.value?.retry()
    }

}