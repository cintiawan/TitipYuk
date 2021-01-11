package xyz.cintiawan.titipyuk.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import io.reactivex.disposables.CompositeDisposable
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.ChatImageItem
import xyz.cintiawan.titipyuk.model.item.ChatItem
import xyz.cintiawan.titipyuk.model.item.ChatTextItem
import xyz.cintiawan.titipyuk.ui.chat.data.ChatRepository
import xyz.cintiawan.titipyuk.util.MessageType
import xyz.cintiawan.titipyuk.util.State

class ChatChannelViewModel(
        private val repository: ChatRepository,
        private val subscriptions: CompositeDisposable
) : BaseViewModel() {
    // Live Data
    private val chatRepositoryLiveData = MutableLiveData<ChatRepository>()
    val userImage = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val buttonEnable = MutableLiveData<Boolean>()

    private lateinit var chatListener: ListenerRegistration
    val stateInit: LiveData<State>
    val errorMessageInit: LiveData<String>

    private var channelId = ""
    private var textMessage = ""
    private var imageMessage = ""

    override fun onCleared() {
        super.onCleared()
        chatListener.remove()
    }

    init {
        buttonEnable.value = false

        chatRepositoryLiveData.value = repository

        stateInit = Transformations.switchMap<ChatRepository, State>(chatRepositoryLiveData, ChatRepository::state)
        errorMessageInit = Transformations.switchMap<ChatRepository, String>(chatRepositoryLiveData, ChatRepository::errorMessage)
    }

    fun init(otherUserId: String, onListen: (List<ChatItem>) -> Unit, onOpen: () -> Unit) {
        repository.openChatChannel(otherUserId) {
            channelId = it.channelId
            chatListener = repository.addChatMessagesListener(it.channelId, onListen)
        }

        repository.getUserInfo(otherUserId) {
            userImage.value = it.image
            userName.value = it.name
        }

        onOpen()
    }

    fun sendText(otherUserId: String) {
        val message = ChatTextItem(
                textMessage,
                System.currentTimeMillis(),
                FirebaseAuth.getInstance().currentUser?.uid,
                otherUserId,
                FirebaseAuth.getInstance().currentUser?.displayName,
                MessageType.TEXT)
        repository.sendMessage(message, channelId, otherUserId)
    }

    fun sendImage(otherUserId: String) {
        subscriptions.add(
                repository.uploadImage(imageMessage)
                        .subscribe({
                            val message = ChatImageItem(
                                    it.message,
                                    System.currentTimeMillis(),
                                    FirebaseAuth.getInstance().currentUser?.uid,
                                    otherUserId,
                                    FirebaseAuth.getInstance().currentUser?.displayName,
                                    MessageType.IMAGE)
                            repository.sendMessage(message, channelId, otherUserId)
                        }, { })
        )
    }

    fun validateText(text: String?) {
        textMessage = text.toString()

        buttonEnable.value = !textMessage.isEmpty()
    }

    fun validateImage(path: String?) {
        imageMessage = path.toString()
    }

}