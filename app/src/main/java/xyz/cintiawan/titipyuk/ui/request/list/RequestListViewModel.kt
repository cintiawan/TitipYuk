package xyz.cintiawan.titipyuk.ui.request.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.RequestItem
import xyz.cintiawan.titipyuk.ui.request.data.RequestDataSource
import xyz.cintiawan.titipyuk.ui.request.data.RequestDataSourceFactory
import xyz.cintiawan.titipyuk.util.State

class RequestListViewModel(
        private val dataSourceFactory: RequestDataSourceFactory,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    val loading = MutableLiveData<Boolean>()

    lateinit var requests: LiveData<PagedList<RequestItem>>

    // Data Source
    private val pageSize = 10
    lateinit var state: LiveData<State>

    init {
        loadRequestListItems()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    private fun loadRequestListItems() {
        // TO BE REPLACED WITH API CALL
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build()

        requests = LivePagedListBuilder<Int, RequestItem>(dataSourceFactory, config).build()
        state = Transformations.switchMap<RequestDataSource, State>(dataSourceFactory.dataSourceLiveData, RequestDataSource::state)
    }

    fun refresh() {
        requests.value?.dataSource?.invalidate()

        loading.value = false
    }

    fun retry() {
        dataSourceFactory.dataSourceLiveData.value?.retry()
    }

}