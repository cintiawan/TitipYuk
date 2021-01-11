package xyz.cintiawan.titipyuk.base

import android.app.Activity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import xyz.cintiawan.titipyuk.di.module.ControllerModule
import xyz.cintiawan.titipyuk.ui.alamat.AlamatSelectBottomSheetFragment
import xyz.cintiawan.titipyuk.ui.preorder.detail.confirm.PreOrderConfirmBottomSheetFragment
import xyz.cintiawan.titipyuk.ui.request.detail.confirm.RequestConfirmBottomSheetFragment
import xyz.cintiawan.titipyuk.ui.varian.VarianBottomSheetFragment

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {
    protected fun inject() {
        val component = (activity?.application as BaseApp).getApplicationComponent()
                .controllerComponent(ControllerModule(activity as Activity))

        when(this) {
            is VarianBottomSheetFragment -> component.inject(this)
            is AlamatSelectBottomSheetFragment -> component.inject(this)
            is PreOrderConfirmBottomSheetFragment -> component.inject(this)
            is RequestConfirmBottomSheetFragment -> component.inject(this)
        }
    }
}