package xyz.cintiawan.titipyuk.ui.titipan.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.Titipan
import xyz.cintiawan.titipyuk.ui.titipan.data.TitipanUserDataSource
import xyz.cintiawan.titipyuk.ui.titipan.data.TitipanUserDataSourceFactory
import xyz.cintiawan.titipyuk.util.State

class TitipanUserListViewModel(
        private val dataSourceFactory: TitipanUserDataSourceFactory,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    val loading = MutableLiveData<Boolean>()

    lateinit var titipans: LiveData<PagedList<Titipan>>

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
        // TO BE REPLACED WITH API CALL
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build()

        titipans = LivePagedListBuilder<Int, Titipan>(dataSourceFactory, config).build()
        state = Transformations.switchMap<TitipanUserDataSource, State>(dataSourceFactory.dataSourceLiveData, TitipanUserDataSource::state)
    }

    fun refresh() {
        titipans.value?.dataSource?.invalidate()

        loading.value = false
    }

    fun retry() {
        dataSourceFactory.dataSourceLiveData.value?.retry()
    }

}