package xyz.cintiawan.titipyuk.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.base.BaseApp
import xyz.cintiawan.titipyuk.db.TitipYukDatabase
import xyz.cintiawan.titipyuk.di.module.ViewModelModule
import xyz.cintiawan.titipyuk.firebase.FirestoreUtil
import xyz.cintiawan.titipyuk.model.item.PreOrderItem
import xyz.cintiawan.titipyuk.model.item.RequestItem
import xyz.cintiawan.titipyuk.model.filter.*
import xyz.cintiawan.titipyuk.ui.beranda.BerandaViewModel
import xyz.cintiawan.titipyuk.ui.city.data.CityRepository
import xyz.cintiawan.titipyuk.ui.kategori.data.KategoriRepository
import xyz.cintiawan.titipyuk.ui.notifikasi.NotifikasiViewModel
import xyz.cintiawan.titipyuk.ui.preorder.data.PreOrderDataSourceFactory
import xyz.cintiawan.titipyuk.ui.preorder.data.PreOrderRepository
import xyz.cintiawan.titipyuk.ui.preorder.detail.PreOrderDetailViewModel
import xyz.cintiawan.titipyuk.ui.preorder.list.PreOrderListViewModel
import xyz.cintiawan.titipyuk.ui.preorder.post.PostPreOrderViewModel
import xyz.cintiawan.titipyuk.ui.varian.VarianViewModel
import xyz.cintiawan.titipyuk.ui.preorder.story.StoryPreOrderViewModel
import xyz.cintiawan.titipyuk.ui.promo.data.PromoRepository
import xyz.cintiawan.titipyuk.ui.review.data.ReviewDataSourceFactory
import xyz.cintiawan.titipyuk.ui.review.list.ReviewListViewModel
import xyz.cintiawan.titipyuk.ui.request.data.RequestDataSourceFactory
import xyz.cintiawan.titipyuk.ui.request.data.RequestRepository
import xyz.cintiawan.titipyuk.ui.request.detail.RequestDetailViewModel
import xyz.cintiawan.titipyuk.ui.request.list.RequestListViewModel
import xyz.cintiawan.titipyuk.ui.request.post.PostRequestViewModel
import xyz.cintiawan.titipyuk.ui.request.story.StoryRequestViewModel
import xyz.cintiawan.titipyuk.ui.slider.SliderViewModel
import xyz.cintiawan.titipyuk.ui.timeline.TimelineViewModel
import xyz.cintiawan.titipyuk.ui.titipan.data.TitipanUserDataSourceFactory
import xyz.cintiawan.titipyuk.ui.titipan.list.TitipanUserListViewModel
import xyz.cintiawan.titipyuk.ui.trip.data.TripDataSourceFactory
import xyz.cintiawan.titipyuk.ui.trip.data.TripRepository
import xyz.cintiawan.titipyuk.ui.trip.detail.TripDetailViewModel
import xyz.cintiawan.titipyuk.ui.trip.list.TripListViewModel
import xyz.cintiawan.titipyuk.ui.trip.post.PostTripViewModel
import xyz.cintiawan.titipyuk.ui.user.auth.LoginViewModel
import xyz.cintiawan.titipyuk.ui.user.auth.RegisterViewModel
import xyz.cintiawan.titipyuk.ui.user.data.UserRepository
import xyz.cintiawan.titipyuk.ui.user.info.UserInfoViewModel
import xyz.cintiawan.titipyuk.ui.user.profile.ProfileViewModel
import xyz.cintiawan.titipyuk.ui.alamat.post.PostAlamatViewModel
import xyz.cintiawan.titipyuk.ui.alamat.AlamatViewModel
import xyz.cintiawan.titipyuk.ui.chat.ChatChannelViewModel
import xyz.cintiawan.titipyuk.ui.chat.LatestChatViewModel
import xyz.cintiawan.titipyuk.ui.chat.data.ChatRepository
import xyz.cintiawan.titipyuk.ui.negara.data.NegaraDataSourceFactory
import xyz.cintiawan.titipyuk.ui.preorder.detail.confirm.PreOrderConfirmViewModel
import xyz.cintiawan.titipyuk.ui.request.detail.confirm.RequestConfirmViewModel
import xyz.cintiawan.titipyuk.ui.trip.detail.confirm.TripConfirmViewModel
import xyz.cintiawan.titipyuk.ui.trip.verifikasi.VerifikasiTripViewModel
import xyz.cintiawan.titipyuk.ui.user.profile.setting.UserSettingViewModel
import javax.inject.Inject

class ViewModelFactory(activity: AppCompatActivity) : ViewModelProvider.Factory {
    @Inject lateinit var api: ApiServiceInterface
    @Inject lateinit var subscriptions: CompositeDisposable
    @Inject lateinit var idler: CountingIdlingResource
    @Inject lateinit var db: TitipYukDatabase

    private lateinit var param: Any

    init {
        val component = (activity.applicationContext as BaseApp)
                .getApplicationComponent()
                .viewModelComponent(ViewModelModule())
        component.inject(this)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SliderViewModel::class.java) -> return SliderViewModel(param as String) as T
            modelClass.isAssignableFrom(BerandaViewModel::class.java) ->
                return BerandaViewModel(
                        PromoRepository(api, idler, db.promoDao()),
                        PreOrderDataSourceFactory(api, subscriptions, idler, db.preOrderDao(), PreOrderFilter()),
                        RequestDataSourceFactory(api, subscriptions, idler, db.requestDao(), RequestFilter()),
                        TripDataSourceFactory(api, subscriptions, idler, db.tripDao(), TripFilter()),
                        NegaraDataSourceFactory(api, subscriptions, idler, db.negaraDao()),
                        subscriptions
                ) as T
            modelClass.isAssignableFrom(TimelineViewModel::class.java) -> return TimelineViewModel(api, subscriptions, idler) as T
            modelClass.isAssignableFrom(NotifikasiViewModel::class.java) -> return  NotifikasiViewModel(api, subscriptions, idler) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> return  ProfileViewModel(UserRepository(api, FirestoreUtil(), idler), subscriptions) as T

            modelClass.isAssignableFrom(PreOrderListViewModel::class.java) ->
                return PreOrderListViewModel(
                        PreOrderDataSourceFactory(api, subscriptions, idler, db.preOrderDao(), param as PreOrderFilter), subscriptions
                ) as T
            modelClass.isAssignableFrom(RequestListViewModel::class.java) ->
                return RequestListViewModel(
                        RequestDataSourceFactory(api, subscriptions, idler, db.requestDao(), param as RequestFilter), subscriptions
                ) as T
            modelClass.isAssignableFrom(TripListViewModel::class.java) ->
                return TripListViewModel(
                        TripDataSourceFactory(api, subscriptions, idler, db.tripDao(), param as TripFilter), subscriptions
                ) as T

            modelClass.isAssignableFrom(StoryPreOrderViewModel::class.java) -> return StoryPreOrderViewModel(param as PreOrderItem) as T
            modelClass.isAssignableFrom(StoryRequestViewModel::class.java) -> return StoryRequestViewModel(param as RequestItem) as T

            modelClass.isAssignableFrom(PreOrderDetailViewModel::class.java) ->
                return PreOrderDetailViewModel(
                        PreOrderRepository(api, idler),
                        subscriptions,
                        param as Int
                ) as T
            modelClass.isAssignableFrom(RequestDetailViewModel::class.java) ->
                return RequestDetailViewModel(
                        RequestRepository(api, idler),
                        subscriptions,
                        param as Int
                ) as T
            modelClass.isAssignableFrom(TripDetailViewModel::class.java) ->
                return TripDetailViewModel(
                        TripRepository(api, idler),
                        subscriptions,
                        param as Int
                ) as T

            modelClass.isAssignableFrom(PostPreOrderViewModel::class.java) -> return PostPreOrderViewModel(PreOrderRepository(api, idler), KategoriRepository(api, idler), CityRepository(api, idler), subscriptions) as T
            modelClass.isAssignableFrom(PostRequestViewModel::class.java) -> return PostRequestViewModel(RequestRepository(api, idler), KategoriRepository(api, idler), CityRepository(api, idler), subscriptions) as T
            modelClass.isAssignableFrom(PostTripViewModel::class.java) -> return PostTripViewModel(TripRepository(api, idler), CityRepository(api, idler), subscriptions) as T

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> return  LoginViewModel(UserRepository(api, FirestoreUtil(), idler), subscriptions) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> return  RegisterViewModel(UserRepository(api, FirestoreUtil(), idler), subscriptions) as T
            modelClass.isAssignableFrom(TitipanUserListViewModel::class.java) ->
                return  TitipanUserListViewModel(
                        TitipanUserDataSourceFactory(api, subscriptions, idler), subscriptions
                ) as T
            modelClass.isAssignableFrom(ReviewListViewModel::class.java) -> return ReviewListViewModel(ReviewDataSourceFactory(api, subscriptions, idler, param as ReviewFilter), subscriptions) as T
            modelClass.isAssignableFrom(UserInfoViewModel::class.java) -> return UserInfoViewModel(api, subscriptions, idler) as T
            modelClass.isAssignableFrom(UserSettingViewModel::class.java) -> return UserSettingViewModel(UserRepository(api, FirestoreUtil(), idler), subscriptions) as T

            modelClass.isAssignableFrom(VarianViewModel::class.java) -> return VarianViewModel() as T
            modelClass.isAssignableFrom(AlamatViewModel::class.java) -> return AlamatViewModel(UserRepository(api, FirestoreUtil(), idler), subscriptions) as T
            modelClass.isAssignableFrom(PostAlamatViewModel::class.java) -> return PostAlamatViewModel(UserRepository(api, FirestoreUtil(), idler), CityRepository(api, idler), subscriptions) as T
            modelClass.isAssignableFrom(PreOrderConfirmViewModel::class.java) -> return PreOrderConfirmViewModel(PreOrderRepository(api, idler), subscriptions) as T
            modelClass.isAssignableFrom(RequestConfirmViewModel::class.java) -> return RequestConfirmViewModel(RequestRepository(api, idler), CityRepository(api, idler), subscriptions) as T
            modelClass.isAssignableFrom(TripConfirmViewModel::class.java) -> return TripConfirmViewModel(TripRepository(api, idler), KategoriRepository(api, idler), subscriptions) as T

            modelClass.isAssignableFrom(VerifikasiTripViewModel::class.java) -> return VerifikasiTripViewModel(TripRepository(api, idler), subscriptions) as T

            modelClass.isAssignableFrom(LatestChatViewModel::class.java) -> return LatestChatViewModel(ChatRepository(api, FirestoreUtil(), idler), subscriptions) as T
            modelClass.isAssignableFrom(ChatChannelViewModel::class.java) -> return ChatChannelViewModel(ChatRepository(api, FirestoreUtil(), idler), subscriptions) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    fun injectParam(param: Any) {
        this.param = param
    }

}