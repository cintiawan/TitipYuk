package xyz.cintiawan.titipyuk.ui.timeline

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.PostItem

class TimelineViewModel(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource
) : BaseViewModel() {
    // Live Data
    val loading = MutableLiveData<Boolean>()
    val posts = MutableLiveData<List<PostItem>>()

    init {
        loadTimelineItems()
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    private fun loadTimelineItems() {
        onLoadTimelineItemsStart()

        // TO BE CHANGED WITH API CALL
        val posts: MutableList<PostItem> = mutableListOf()

        onLoadTimelineItemsFinish()
        onLoadTimelineItemsSuccess(posts)
    }

    private fun onLoadTimelineItemsStart() {
        loading.value = true
    }

    private fun onLoadTimelineItemsFinish() {
        loading.value = false
    }

    private fun onLoadTimelineItemsSuccess(data: List<PostItem>) {
        posts.value = data
    }

}