package xyz.cintiawan.titipyuk.ui.beranda

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.*
import xyz.cintiawan.titipyuk.ui.negara.data.NegaraDataSource
import xyz.cintiawan.titipyuk.ui.negara.data.NegaraDataSourceFactory
import xyz.cintiawan.titipyuk.ui.preorder.data.PreOrderDataSource
import xyz.cintiawan.titipyuk.ui.preorder.data.PreOrderDataSourceFactory
import xyz.cintiawan.titipyuk.ui.promo.data.PromoRepository
import xyz.cintiawan.titipyuk.ui.request.data.RequestDataSource
import xyz.cintiawan.titipyuk.ui.request.data.RequestDataSourceFactory
import xyz.cintiawan.titipyuk.ui.trip.data.TripDataSource
import xyz.cintiawan.titipyuk.ui.trip.data.TripDataSourceFactory
import xyz.cintiawan.titipyuk.util.State

class BerandaViewModel(
        private val promoRepository: PromoRepository,
        private val preOrderDataSourceFactory: PreOrderDataSourceFactory,
        private val requestDataSourceFactory: RequestDataSourceFactory,
        private val tripDataSourceFactory: TripDataSourceFactory,
        private val negaraDataSourceFactory: NegaraDataSourceFactory,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    private val promoRepositoryLiveData = MutableLiveData<PromoRepository>()
    val refreshing = MutableLiveData<Boolean>()
    val sliderImages = MutableLiveData<List<String>>()

    val preorders: LiveData<PagedList<PreOrderItem>>
    val requests: LiveData<PagedList<RequestItem>>
    val trips: LiveData<PagedList<TripItem>>
    val negaras: LiveData<PagedList<NegaraItem>>

    // Data Source
    private val pageSize = 10
    val preOrderState: LiveData<State>
    val requestState: LiveData<State>
    val tripState: LiveData<State>
    val negaraState: LiveData<State>
    val imageState: LiveData<State>

    init {
        promoRepositoryLiveData.value = promoRepository
        imageState = Transformations.switchMap<PromoRepository, State>(promoRepositoryLiveData, PromoRepository::state)

        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(true)
                .build()

        preorders = LivePagedListBuilder<Int, PreOrderItem>(preOrderDataSourceFactory, config).build()
        preOrderState = Transformations.switchMap<PreOrderDataSource, State>(preOrderDataSourceFactory.dataSourceLiveData, PreOrderDataSource::state)

        requests = LivePagedListBuilder<Int, RequestItem>(requestDataSourceFactory, config).build()
        requestState = Transformations.switchMap<RequestDataSource, State>(requestDataSourceFactory.dataSourceLiveData, RequestDataSource::state)

        trips = LivePagedListBuilder<Int, TripItem>(tripDataSourceFactory, config).build()
        tripState = Transformations.switchMap<TripDataSource, State>(tripDataSourceFactory.dataSourceLiveData, TripDataSource::state)

        negaras = LivePagedListBuilder<Int, NegaraItem>(negaraDataSourceFactory, config).build()
        negaraState = Transformations.switchMap<NegaraDataSource, State>(negaraDataSourceFactory.dataSourceLiveData, NegaraDataSource::state)

        loadSliderImages()
    }

    override fun onCleared() {
        subscriptions.dispose()
        super.onCleared()
    }

    fun loadSliderImages() {
        subscriptions.add(promoRepository.getPromos()
                .subscribe ({ response ->
                    val images = mutableListOf<String>()
                    response.data.forEach { images.add(it.bannerPath) }
                    sliderImages.value = images
                }, {
                    Log.d("apalah_ini_error", it.message)
                })
        )
    }

    fun refresh() {
        loadSliderImages()
        preorders.value?.dataSource?.invalidate()
        requests.value?.dataSource?.invalidate()
        trips.value?.dataSource?.invalidate()
        negaras.value?.dataSource?.invalidate()

        refreshing.value = false
    }

    fun preOrderRetry() {
        preOrderDataSourceFactory.dataSourceLiveData.value?.retry()
    }

    fun requestRetry() {
        requestDataSourceFactory.dataSourceLiveData.value?.retry()
    }

    fun tripRetry() {
        tripDataSourceFactory.dataSourceLiveData.value?.retry()
    }

    fun cityRetry() {
        negaraDataSourceFactory.dataSourceLiveData.value?.retry()
    }

}