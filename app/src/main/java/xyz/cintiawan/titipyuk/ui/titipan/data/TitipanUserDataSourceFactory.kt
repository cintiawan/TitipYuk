package xyz.cintiawan.titipyuk.ui.titipan.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.model.Titipan

class TitipanUserDataSourceFactory(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource
) : DataSource.Factory<Int, Titipan>() {
    val dataSourceLiveData = MutableLiveData<TitipanUserDataSource>()

    override fun create(): DataSource<Int, Titipan> {
        val dataSource = TitipanUserDataSource(api, subscriptions, idler)
        dataSourceLiveData.postValue(dataSource)

        return dataSource
    }

}