package xyz.cintiawan.titipyuk.base

import android.app.Activity
import androidx.fragment.app.Fragment
import xyz.cintiawan.titipyuk.di.module.ControllerModule
import xyz.cintiawan.titipyuk.ui.beranda.BerandaFragment
import xyz.cintiawan.titipyuk.ui.notifikasi.NotifikasiFragment
import xyz.cintiawan.titipyuk.ui.preorder.list.PreOrderListFragment
import xyz.cintiawan.titipyuk.ui.preorder.story.StoryPreOrderFragment
import xyz.cintiawan.titipyuk.ui.request.list.RequestListFragment
import xyz.cintiawan.titipyuk.ui.request.story.StoryRequestFragment
import xyz.cintiawan.titipyuk.ui.slider.SliderBarangFragment
import xyz.cintiawan.titipyuk.ui.slider.SliderFragment
import xyz.cintiawan.titipyuk.ui.timeline.TimelineFragment
import xyz.cintiawan.titipyuk.ui.trip.list.TripListFragment
import xyz.cintiawan.titipyuk.ui.user.auth.LoginFragment
import xyz.cintiawan.titipyuk.ui.user.auth.RedirectFragment
import xyz.cintiawan.titipyuk.ui.user.auth.RegisterFragment
import xyz.cintiawan.titipyuk.ui.user.profile.UserFragment

abstract class BaseFragment : Fragment() {
    protected fun inject() {
        val component = (activity?.application as BaseApp).getApplicationComponent()
                .controllerComponent(ControllerModule(activity as Activity))

        when(this) {
            is SliderFragment -> component.inject(this)
            is SliderBarangFragment -> component.inject(this)
            is BerandaFragment -> component.inject(this)
            is TimelineFragment -> component.inject(this)
            is NotifikasiFragment -> component.inject(this)
            is UserFragment -> component.inject(this)
            is PreOrderListFragment -> component.inject(this)
            is RequestListFragment -> component.inject(this)
            is TripListFragment -> component.inject(this)
            is StoryPreOrderFragment -> component.inject(this)
            is StoryRequestFragment -> component.inject(this)
            is LoginFragment -> component.inject(this)
            is RegisterFragment -> component.inject(this)
        }
    }
}