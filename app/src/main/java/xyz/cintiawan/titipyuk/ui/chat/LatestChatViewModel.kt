package xyz.cintiawan.titipyuk.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.firestore.ListenerRegistration
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.ChatChannelItem
import xyz.cintiawan.titipyuk.ui.chat.data.ChatRepository
import xyz.cintiawan.titipyuk.util.State

class LatestChatViewModel(
        private val repository: ChatRepository,
        private val subscriptions: CompositeDisposable
): BaseViewModel() {
    // Live Data
    private val chatRepositoryLiveData = MutableLiveData<ChatRepository>()
    val userImage = MutableLiveData<String>()
    val userName = MutableLiveData<String>()

    private lateinit var channelListener: ListenerRegistration
    val stateInit: LiveData<State>
    val errorMessageInit: LiveData<String>

    private var channelId = ""

    override fun onCleared() {
        super.onCleared()
        channelListener.remove()
    }

    init {
        chatRepositoryLiveData.value = repository

        stateInit = Transformations.switchMap<ChatRepository, State>(chatRepositoryLiveData, ChatRepository::state)
        errorMessageInit = Transformations.switchMap<ChatRepository, String>(chatRepositoryLiveData, ChatRepository::errorMessage)
    }

    fun init(onListen: (List<ChatChannelItem>) -> Unit) {
        channelListener = repository.addChatChannelsListener { onListen(it) }
    }

}