package xyz.cintiawan.titipyuk.ui.notifikasi

import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.base.BaseViewModel

class NotifikasiViewModel(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource
) : BaseViewModel() {
}