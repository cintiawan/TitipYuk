package xyz.cintiawan.titipyuk.ui.preorder.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.model.filter.PreOrderFilter
import xyz.cintiawan.titipyuk.model.item.PreOrderItem

class PreOrderDataSourceFactory(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource,
        private val dao: PreOrderDao,
        private val filter: PreOrderFilter
) : DataSource.Factory<Int, PreOrderItem>() {
    val dataSourceLiveData = MutableLiveData<PreOrderDataSource>()

    override fun create(): DataSource<Int, PreOrderItem> {
        val dataSource = PreOrderDataSource(api, subscriptions, idler, dao, filter)
        dataSourceLiveData.postValue(dataSource)

        return dataSource
    }

}