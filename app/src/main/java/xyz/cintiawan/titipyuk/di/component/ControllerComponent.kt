package xyz.cintiawan.titipyuk.di.component

import dagger.Subcomponent
import xyz.cintiawan.titipyuk.di.module.ControllerModule
import xyz.cintiawan.titipyuk.di.scope.ControllerScope
import xyz.cintiawan.titipyuk.ui.beranda.BerandaFragment
import xyz.cintiawan.titipyuk.ui.intro.IntroActivity
import xyz.cintiawan.titipyuk.ui.list.ListActivity
import xyz.cintiawan.titipyuk.ui.main.MainActivity
import xyz.cintiawan.titipyuk.ui.notifikasi.NotifikasiFragment
import xyz.cintiawan.titipyuk.ui.preorder.detail.PreOrderDetailActivity
import xyz.cintiawan.titipyuk.ui.preorder.list.PreOrderListFragment
import xyz.cintiawan.titipyuk.ui.preorder.list.PreOrderVerticalListActivity
import xyz.cintiawan.titipyuk.ui.preorder.post.PostPreOrderActivity
import xyz.cintiawan.titipyuk.ui.varian.VarianBottomSheetFragment
import xyz.cintiawan.titipyuk.ui.preorder.story.StoryPreOrderActivity
import xyz.cintiawan.titipyuk.ui.preorder.story.StoryPreOrderFragment
import xyz.cintiawan.titipyuk.ui.review.list.ReviewUserListActivity
import xyz.cintiawan.titipyuk.ui.request.detail.RequestDetailActivity
import xyz.cintiawan.titipyuk.ui.request.list.RequestListFragment
import xyz.cintiawan.titipyuk.ui.request.list.RequestVerticalListActivity
import xyz.cintiawan.titipyuk.ui.request.post.PostRequestActivity
import xyz.cintiawan.titipyuk.ui.request.story.StoryRequestActivity
import xyz.cintiawan.titipyuk.ui.request.story.StoryRequestFragment
import xyz.cintiawan.titipyuk.ui.slider.SliderBarangFragment
import xyz.cintiawan.titipyuk.ui.slider.SliderFragment
import xyz.cintiawan.titipyuk.ui.splash.SplashActivity
import xyz.cintiawan.titipyuk.ui.timeline.TimelineFragment
import xyz.cintiawan.titipyuk.ui.titipan.list.TitipanUserListActivity
import xyz.cintiawan.titipyuk.ui.trip.detail.TripDetailActivity
import xyz.cintiawan.titipyuk.ui.trip.list.TripListFragment
import xyz.cintiawan.titipyuk.ui.trip.list.TripVerticalListActivity
import xyz.cintiawan.titipyuk.ui.trip.post.PostTripActivity
import xyz.cintiawan.titipyuk.ui.user.auth.LoginFragment
import xyz.cintiawan.titipyuk.ui.user.auth.RedirectFragment
import xyz.cintiawan.titipyuk.ui.user.auth.RegisterFragment
import xyz.cintiawan.titipyuk.ui.user.info.UserInfoActivity
import xyz.cintiawan.titipyuk.ui.user.profile.UserFragment
import xyz.cintiawan.titipyuk.ui.alamat.post.PostAlamatActivity
import xyz.cintiawan.titipyuk.ui.alamat.AlamatSelectBottomSheetFragment
import xyz.cintiawan.titipyuk.ui.alamat.SettingAlamatActivity
import xyz.cintiawan.titipyuk.ui.chat.ChatChannelActivity
import xyz.cintiawan.titipyuk.ui.chat.LatestChatActivity
import xyz.cintiawan.titipyuk.ui.preorder.detail.confirm.PreOrderConfirmBottomSheetFragment
import xyz.cintiawan.titipyuk.ui.request.detail.confirm.RequestConfirmBottomSheetFragment
import xyz.cintiawan.titipyuk.ui.trip.detail.confirm.TripConfirmActivity
import xyz.cintiawan.titipyuk.ui.trip.verifikasi.VerifikasiTripActivity
import xyz.cintiawan.titipyuk.ui.user.profile.setting.UserSettingActivity

@ControllerScope
@Subcomponent(modules = [ControllerModule::class])
interface ControllerComponent {
    fun inject(splashActivity: SplashActivity)
    fun inject(introActivity: IntroActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(preOrderDetailActivity: PreOrderDetailActivity)
    fun inject(requestDetailActivity: RequestDetailActivity)
    fun inject(tripDetailActivity: TripDetailActivity)
    fun inject(titipanUserListActivity: TitipanUserListActivity)
    fun inject(preOrderVerticalListActivity: PreOrderVerticalListActivity)
    fun inject(requestVerticalListActivity: RequestVerticalListActivity)
    fun inject(tripVerticalListActivity: TripVerticalListActivity)
    fun inject(postPreOrderActivity: PostPreOrderActivity)
    fun inject(postRequestActivity: PostRequestActivity)
    fun inject(postTripActivity: PostTripActivity)
    fun inject(ratingListActivity: ReviewUserListActivity)
    fun inject(userInfoActivity: UserInfoActivity)
    fun inject(userSettingActivity: UserSettingActivity)
    fun inject(settingAlamatActivity: SettingAlamatActivity)
    fun inject(postAlamatActivity: PostAlamatActivity)
    fun inject(tripConfirmActivity: TripConfirmActivity)
    fun inject(verifikasiTripActivity: VerifikasiTripActivity)
    fun inject(latestChatActivity: LatestChatActivity)
    fun inject(chatChannelActivity: ChatChannelActivity)

    fun inject(sliderFragment: SliderFragment)
    fun inject(sliderBarangFragment: SliderBarangFragment)
    fun inject(berandaFragment: BerandaFragment)
    fun inject(timelineFragment: TimelineFragment)
    fun inject(notifikasiFragment: NotifikasiFragment)
    fun inject(userFragment: UserFragment)
    fun inject(preOrderListFragment: PreOrderListFragment)
    fun inject(requestListFragment: RequestListFragment)
    fun inject(tripListFragment: TripListFragment)
    fun inject(storyPreOrderFragment: StoryPreOrderFragment)
    fun inject(storyRequestFragment: StoryRequestFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(registerFragment: RegisterFragment)

    fun inject(varianBottomSheetFragment: VarianBottomSheetFragment)
    fun inject(selectBottomSheetFragment: AlamatSelectBottomSheetFragment)
    fun inject(preOrderConfirmBottomSheetFragment: PreOrderConfirmBottomSheetFragment)
    fun inject(requestConfirmBottomSheetFragment: RequestConfirmBottomSheetFragment)
}