package xyz.cintiawan.titipyuk.ui.negara.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.model.item.NegaraItem

class NegaraDataSourceFactory(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource,
        private val dao: NegaraDao
) : DataSource.Factory<Int, NegaraItem>() {
    val dataSourceLiveData = MutableLiveData<NegaraDataSource>()

    override fun create(): DataSource<Int, NegaraItem> {
        val dataSource = NegaraDataSource(api, subscriptions, idler, dao)
        dataSourceLiveData.postValue(dataSource)

        return dataSource
    }

}