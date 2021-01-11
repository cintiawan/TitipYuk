package xyz.cintiawan.titipyuk.ui.user.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.detail.UserDetail
import xyz.cintiawan.titipyuk.ui.user.data.UserRepository
import xyz.cintiawan.titipyuk.util.State

class ProfileViewModel(
        private val repository: UserRepository,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    private val userRepositoryLiveData = MutableLiveData<UserRepository>()
    val userName = MutableLiveData<String>()
    val userFollowers = MutableLiveData<String>()
    val userFollowing = MutableLiveData<String>()
    val userImage = MutableLiveData<String>()

    val state: LiveData<State>
    val errorMessage: LiveData<String>
    private var completable: Completable? = null

    init {
        userRepositoryLiveData.value = repository

        state = Transformations.switchMap<UserRepository, State>(userRepositoryLiveData, UserRepository::state)
        errorMessage = Transformations.switchMap<UserRepository, String>(userRepositoryLiveData, UserRepository::errorMessage)

        loadUserInfo()
    }

    override fun onCleared() {
        subscriptions.dispose()
        super.onCleared()
    }

    private fun loadUserInfo() {
        subscriptions.add(repository.myInfo()
                .subscribe({ response ->
                    onLoadMyInfoSuccess(response)
                }, {
                    setRetry(Action { loadUserInfo() })
                })
        )
    }

    private fun setRetry(action: Action?) {
        completable = if(action == null) null else Completable.fromAction(action)
    }

    private fun onLoadMyInfoSuccess(data: UserDetail) {
        userName.value = data.name
        userFollowers.value = data.follower.toString()
        userFollowing.value = data.following.toString()
        userImage.value = data.image
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