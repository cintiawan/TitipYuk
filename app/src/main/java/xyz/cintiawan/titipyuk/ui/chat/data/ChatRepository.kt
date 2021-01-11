package xyz.cintiawan.titipyuk.ui.chat.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.idling.CountingIdlingResource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import xyz.cintiawan.titipyuk.api.ApiServiceInterface
import xyz.cintiawan.titipyuk.firebase.FirestoreUtil
import xyz.cintiawan.titipyuk.model.Message
import xyz.cintiawan.titipyuk.model.detail.ChatChannelDetail
import xyz.cintiawan.titipyuk.model.item.*
import xyz.cintiawan.titipyuk.util.MessageType
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.toErrorMessage
import xyz.cintiawan.titipyuk.util.toMultipartBody
import java.lang.Exception

class ChatRepository(
        private val api: ApiServiceInterface,
        private val firestore: FirestoreUtil,
        private val idler: CountingIdlingResource
) {
    val state = MutableLiveData<State>()
    val errorMessage = MutableLiveData<String>()

    fun uploadImage(path: String): Single<Message> =
            api.uploadImage(path.toMultipartBody("gambar"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { onStart() }
                    .doOnSuccess { onSuccess() }
                    .doOnError { onError(it) }
                    .doFinally { onFinish() }

    fun addUsersListener(onListen: (List<UserFirebaseItem>) -> Unit): ListenerRegistration {
        return firestore.usersCollectionRef()
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if(firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", firebaseFirestoreException.message)
                        return@addSnapshotListener
                    }

                    val data = mutableListOf<UserFirebaseItem>()
                    querySnapshot?.documents?.forEach { doc ->
                        if(doc.id != FirebaseAuth.getInstance().currentUser?.uid)
                            doc.toObject(UserFirebaseItem::class.java)?.let { data.add(it) }
                    }
                    onListen(data)
                }
    }

    fun addChatChannelsListener(onListen: (List<ChatChannelItem>) -> Unit): ListenerRegistration {
        return firestore.currentUserEngagedChannelsCollectionRef()
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if(firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", firebaseFirestoreException.message)
                        return@addSnapshotListener
                    }

                    try {
                        val data = mutableListOf<ChatChannelItem>()
                        querySnapshot?.documents?.forEach { doc ->
                            doc.toObject(ChatChannelFirebaseItem::class.java)?.let { channel ->
                                getUserInfo(channel.otherUserUid) {
                                    val message: ChatItem? =
                                            if (channel.lastMessageType == MessageType.TEXT)
                                                ChatTextItem(channel.lastMessage, channel.lastMessageTimestamp, channel.senderUid, "", "", channel.lastMessageType)
                                            else
                                                ChatImageItem(channel.lastMessage, channel.lastMessageTimestamp, channel.senderUid, "", "", channel.lastMessageType)
                                    data.add(ChatChannelItem(channel.channelId, message, channel.otherUserUid, channel.senderUid, it.image, it.name, channel.unseen))
                                    onListen(data)
                                }
                            }
                        }
                    } catch (e: Exception) { }
                }
    }

    fun getUserInfo(userId: String, onComplete: (UserFirebaseItem) -> Unit) {
        firestore.otherUserDocRef(userId)
                .get()
                .addOnSuccessListener { doc -> doc.toObject(UserFirebaseItem::class.java)?.let { onComplete(it) } }
    }

    fun openChatChannel(otherUserId: String, onComplete: (ChatChannelFirebaseItem) -> Unit) {
        onStart()
        firestore.currentUserEngagedChannelsCollectionRef()
                .document(otherUserId)
                .get()
                .addOnSuccessListener { doc ->
                    onSuccess()
                    if(doc.exists()) {
                        doc.toObject(ChatChannelFirebaseItem::class.java)?.let { onComplete(it) }
                        return@addOnSuccessListener
                    }

                    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                    val newChannel = firestore.chatChannelsCollectionRef().document()

                    newChannel.set(ChatChannelDetail(mutableListOf(currentUserId!!, otherUserId)))

                    val chatChannelOther = ChatChannelFirebaseItem(newChannel.id, "", -1, MessageType.TEXT, currentUserId, currentUserId, 0)
                    firestore.otherUserEngagedChannelsCollectionRef(otherUserId)
                            .document(currentUserId)
                            .set(chatChannelOther)
                    val chatChannelCurrent = ChatChannelFirebaseItem(newChannel.id, "", -1, MessageType.TEXT, currentUserId, currentUserId, 0)
                    firestore.currentUserEngagedChannelsCollectionRef()
                            .document(otherUserId)
                            .set(chatChannelCurrent)

                    onComplete(chatChannelCurrent)
                }
                .addOnFailureListener { onError(it.message.toString()) }
                .addOnCompleteListener { onFinish() }
    }

    fun addChatMessagesListener(channelId: String, onListen: (List<ChatItem>) -> Unit): ListenerRegistration {
        return firestore.chatChannelMessagesCollectionRef(channelId)
                .orderBy("timestamp")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.e("FIRESTORE", firebaseFirestoreException.message)
                        return@addSnapshotListener
                    }

                    val data = mutableListOf<ChatItem>()
                    querySnapshot?.documents?.forEach { doc ->
                        if(doc["type"] == MessageType.TEXT)
                            doc.toObject(ChatTextItem::class.java)?.let { data.add(it) }
                        else if(doc["type"] == MessageType.IMAGE)
                            doc.toObject(ChatImageItem::class.java)?.let { data.add(it) }
                    }
                    onListen(data)
                }
    }

    fun sendMessage(message: ChatItem, channelId: String, otherUserId: String) {
        firestore.chatChannelMessagesCollectionRef(channelId)
                .add(message)

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        val chatChannelOther = ChatChannelFirebaseItem(
                channelId,
                if(message.type == MessageType.TEXT) (message as ChatTextItem).text else "(Mengirimkan Gambar)",
                System.currentTimeMillis(),
                message.type,
                currentUserId!!,
                currentUserId,
                1)
        firestore.otherUserEngagedChannelsCollectionRef(otherUserId)
                .document(currentUserId)
                .set(chatChannelOther)
        val chatChannelCurrent = ChatChannelFirebaseItem(
                channelId,
                if(message.type == MessageType.TEXT) (message as ChatTextItem).text else "(Mengirimkan Gambar)",
                System.currentTimeMillis(),
                message.type,
                otherUserId,
                currentUserId,
                0)
        firestore.currentUserEngagedChannelsCollectionRef()
                .document(otherUserId)
                .set(chatChannelCurrent)
    }

    private fun onStart() {
        idler.increment()
        state.postValue(State.LOADING)
    }

    private fun onSuccess() {
        state.postValue(State.DONE)
    }

    private fun onError(error: Throwable) {
        Log.d("apalah_ini_error", error.message)
        state.postValue(State.ERROR)

        if(error is HttpException) {
            errorMessage.postValue(error.toErrorMessage())
        } else {
            errorMessage.postValue("Telah terjadi kesalahan")
        }
    }

    private fun onError(msg: String) {
        Log.d("apalah_ini_error", msg)
        state.postValue(State.ERROR)

        errorMessage.postValue(msg)
    }

    private fun onFinish() {
        idler.decrement()
    }

}