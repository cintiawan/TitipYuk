package xyz.cintiawan.titipyuk.ui.user.info

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.idling.CountingIdlingResource
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.util.toDays
import xyz.cintiawan.titipyuk.util.toHours
import xyz.cintiawan.titipyuk.util.toMinutes

class UserInfoViewModel(
        private val api: ApiServiceInterface,
        private val subscriptions: CompositeDisposable,
        private val idler: CountingIdlingResource
) : BaseViewModel() {
    // Live Data
    val userImage = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val userRating = MutableLiveData<String>()
    val userFollowers = MutableLiveData<String>()
    val userFollowing = MutableLiveData<String>()
    val userLastOnline = MutableLiveData<String>()
    val follow = MutableLiveData<String>()
    val userDescription = MutableLiveData<String>()

    init {
        loadUserInfo()
    }

    override fun onCleared() {
        subscriptions.dispose()
        super.onCleared()
    }

    private fun loadUserInfo() {

    }

    private fun lastOnline(passed: Long): String {
        var result = "Online: "
        when {
            passed.toDays() > 0 -> result += "${passed.toDays()} Hari yang lalu"
            passed.toHours() > 0 -> result += "${passed.toHours()} Jam yang lalu"
            passed.toMinutes() > 0 -> result += "${passed.toMinutes()} Menit yang lalu"
            else -> result = "Sedang Online"
        }

        return result
    }

}