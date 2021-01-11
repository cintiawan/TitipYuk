package xyz.cintiawan.titipyuk.base

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import xyz.cintiawan.titipyuk.di.module.ControllerModule
import xyz.cintiawan.titipyuk.ui.intro.IntroActivity
import xyz.cintiawan.titipyuk.ui.list.ListActivity
import xyz.cintiawan.titipyuk.ui.main.MainActivity
import xyz.cintiawan.titipyuk.ui.preorder.detail.PreOrderDetailActivity
import xyz.cintiawan.titipyuk.ui.preorder.list.PreOrderVerticalListActivity
import xyz.cintiawan.titipyuk.ui.preorder.post.PostPreOrderActivity
import xyz.cintiawan.titipyuk.ui.preorder.story.StoryPreOrderActivity
import xyz.cintiawan.titipyuk.ui.review.list.ReviewUserListActivity
import xyz.cintiawan.titipyuk.ui.request.detail.RequestDetailActivity
import xyz.cintiawan.titipyuk.ui.request.list.RequestVerticalListActivity
import xyz.cintiawan.titipyuk.ui.request.post.PostRequestActivity
import xyz.cintiawan.titipyuk.ui.request.story.StoryRequestActivity
import xyz.cintiawan.titipyuk.ui.splash.SplashActivity
import xyz.cintiawan.titipyuk.ui.titipan.list.TitipanUserListActivity
import xyz.cintiawan.titipyuk.ui.trip.detail.TripDetailActivity
import xyz.cintiawan.titipyuk.ui.trip.list.TripVerticalListActivity
import xyz.cintiawan.titipyuk.ui.trip.post.PostTripActivity
import xyz.cintiawan.titipyuk.ui.user.info.UserInfoActivity
import xyz.cintiawan.titipyuk.ui.alamat.post.PostAlamatActivity
import xyz.cintiawan.titipyuk.ui.alamat.SettingAlamatActivity
import xyz.cintiawan.titipyuk.ui.chat.ChatChannelActivity
import xyz.cintiawan.titipyuk.ui.chat.LatestChatActivity
import xyz.cintiawan.titipyuk.ui.trip.detail.confirm.TripConfirmActivity
import xyz.cintiawan.titipyuk.ui.trip.verifikasi.VerifikasiTripActivity
import xyz.cintiawan.titipyuk.ui.user.profile.setting.UserSettingActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    protected fun inject() {
        val component = (application as BaseApp).getApplicationComponent()
                .controllerComponent(ControllerModule(this as Activity))

        when(this) {
            is SplashActivity -> component.inject(this)
            is IntroActivity -> component.inject(this)
            is MainActivity -> component.inject(this)
            is PreOrderDetailActivity -> component.inject(this)
            is RequestDetailActivity -> component.inject(this)
            is TripDetailActivity -> component.inject(this)
            is TitipanUserListActivity -> component.inject(this)
            is PreOrderVerticalListActivity -> component.inject(this)
            is RequestVerticalListActivity -> component.inject(this)
            is TripVerticalListActivity -> component.inject(this)
            is PostPreOrderActivity -> component.inject(this)
            is PostRequestActivity -> component.inject(this)
            is PostTripActivity -> component.inject(this)
            is ReviewUserListActivity -> component.inject(this)
            is UserInfoActivity -> component.inject(this)
            is UserSettingActivity -> component.inject(this)
            is SettingAlamatActivity -> component.inject(this)
            is PostAlamatActivity -> component.inject(this)
            is TripConfirmActivity -> component.inject(this)
            is VerifikasiTripActivity -> component.inject(this)
            is LatestChatActivity -> component.inject(this)
            is ChatChannelActivity -> component.inject(this)
        }
    }
}