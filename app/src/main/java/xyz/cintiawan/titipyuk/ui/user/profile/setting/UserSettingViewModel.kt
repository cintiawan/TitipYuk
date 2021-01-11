package xyz.cintiawan.titipyuk.ui.user.profile.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.ui.user.data.UserRepository
import xyz.cintiawan.titipyuk.util.State

class UserSettingViewModel(
        private val repository: UserRepository,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    private val userRepositoryLiveData = MutableLiveData<UserRepository>()
    val state: LiveData<State>
    val errorMessage: LiveData<String>
    val successMessage = MutableLiveData<String>()

    private var completable: Completable? = null

    override fun onCleared() {
        super.onCleared()
        subscriptions.dispose()
    }

    init {
        userRepositoryLiveData.value = repository

        state = Transformations.switchMap<UserRepository, State>(userRepositoryLiveData, UserRepository::state)
        errorMessage = Transformations.switchMap<UserRepository, String>(userRepositoryLiveData, UserRepository::errorMessage)
    }

    fun logout(onComplete: () -> Unit) {
        subscriptions.add(
                repository.logout()
                        .subscribe({
                            onComplete()
                            successMessage.value = "Anda telah berhasil logout"
                        }, {
                            setRetry(Action { logout(onComplete) })
                        })
        )
    }

    private fun setRetry(action: Action?) {
        completable = if(action == null) null else Completable.fromAction(action)
    }

    fun retry() {
        completable?.let {
            subscriptions.add(it
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe())
        }
    }

}