package xyz.cintiawan.titipyuk.ui.review.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.model.item.ReviewItem
import xyz.cintiawan.titipyuk.model.filter.ReviewFilter

class ReviewDataSourceFactory(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource,
        private val filter: ReviewFilter
) : DataSource.Factory<Int, ReviewItem>() {
    val dataSourceLiveData = MutableLiveData<ReviewDataSource>()

    override fun create(): DataSource<Int, ReviewItem> {
        val dataSource = ReviewDataSource(api, subscriptions, idler, filter)
        dataSourceLiveData.postValue(dataSource)

        return dataSource
    }

}