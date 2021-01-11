package xyz.cintiawan.titipyuk.ui.city.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.model.item.CityItem

class CityDataSourceFactory(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource,
        private val dao: CityDao
) : DataSource.Factory<Int, CityItem>() {
    val dataSourceLiveData = MutableLiveData<CityDataSource>()

    override fun create(): DataSource<Int, CityItem> {
        val dataSource = CityDataSource(api, subscriptions, idler, dao)
        dataSourceLiveData.postValue(dataSource)

        return dataSource
    }

}