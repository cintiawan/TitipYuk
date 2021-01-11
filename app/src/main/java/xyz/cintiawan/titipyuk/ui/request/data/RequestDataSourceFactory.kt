package xyz.cintiawan.titipyuk.ui.request.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.model.filter.RequestFilter
import xyz.cintiawan.titipyuk.model.item.RequestItem

class RequestDataSourceFactory(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource,
        private val dao: RequestDao,
        private val filter: RequestFilter
) : DataSource.Factory<Int, RequestItem>() {
    val dataSourceLiveData = MutableLiveData<RequestDataSource>()

    override fun create(): DataSource<Int, RequestItem> {
        val dataSource = RequestDataSource(api, subscriptions, idler, dao, filter)
        dataSourceLiveData.postValue(dataSource)

        return dataSource
    }

}