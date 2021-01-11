package xyz.cintiawan.titipyuk.ui.trip.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.model.filter.TripFilter
import xyz.cintiawan.titipyuk.model.item.TripItem

class TripDataSourceFactory(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource,
        private val dao: TripDao,
        private val filter: TripFilter
) : DataSource.Factory<Int, TripItem>() {
    val dataSourceLiveData = MutableLiveData<TripDataSource>()

    override fun create(): DataSource<Int, TripItem> {
        val dataSource = TripDataSource(api, subscriptions, idler, dao, filter)
        dataSourceLiveData.postValue(dataSource)

        return dataSource
    }

}