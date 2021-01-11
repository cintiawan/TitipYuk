package xyz.cintiawan.titipyuk.ui.preorder.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.PreOrderItem
import xyz.cintiawan.titipyuk.ui.preorder.data.PreOrderDataSource
import xyz.cintiawan.titipyuk.ui.preorder.data.PreOrderDataSourceFactory
import xyz.cintiawan.titipyuk.util.State

class PreOrderListViewModel(
        private val dataSourceFactory: PreOrderDataSourceFactory,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    val loading = MutableLiveData<Boolean>()

    lateinit var preorders: LiveData<PagedList<PreOrderItem>>

    // Data Source
    private val pageSize = 10
    lateinit var state: LiveData<State>

    init {
        loadPreOrderListItems()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    private fun loadPreOrderListItems() {
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build()

        preorders = LivePagedListBuilder<Int, PreOrderItem>(dataSourceFactory, config).build()
        state = Transformations.switchMap<PreOrderDataSource, State>(dataSourceFactory.dataSourceLiveData, PreOrderDataSource::state)
    }

    fun refresh() {
        preorders.value?.dataSource?.invalidate()

        loading.value = false
    }

    fun retry() {
        dataSourceFactory.dataSourceLiveData.value?.retry()
    }

}