package xyz.cintiawan.titipyuk.ui.chat.item.channel

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.ChatChannelItem
import xyz.cintiawan.titipyuk.model.item.ChatTextItem
import xyz.cintiawan.titipyuk.util.MessageType
import xyz.cintiawan.titipyuk.util.millisToDateTime

class ItemChannelViewModel : BaseViewModel() {
    val latestMessage = MutableLiveData<String>()
    val userImage = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val unseen = MutableLiveData<String>()
    val notificationVisibility = MutableLiveData<Boolean>()

    fun bind(data: ChatChannelItem?) {
        if(data?.lastMessage?.type == MessageType.TEXT) latestMessage.value = (data.lastMessage as ChatTextItem).text else if(data?.lastMessage?.type == MessageType.IMAGE) latestMessage.value = "(Pesan Berupa Gambar)"
        userImage.value = data?.otherUserImage
        userName.value = data?.otherUserName
        date.value = data?.lastMessage?.timestamp?.millisToDateTime()
        unseen.value = data?.unseen.toString()
        data?.unseen?.let { notificationVisibility.value = it > 0 }
    }
}