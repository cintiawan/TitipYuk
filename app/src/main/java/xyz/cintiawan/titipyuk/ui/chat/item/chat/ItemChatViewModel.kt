package xyz.cintiawan.titipyuk.ui.chat.item.chat

import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.ChatImageItem
import xyz.cintiawan.titipyuk.model.item.ChatItem
import xyz.cintiawan.titipyuk.model.item.ChatTextItem
import xyz.cintiawan.titipyuk.util.millisToDateTime

class ItemChatViewModel : BaseViewModel() {
    val textMessage = MutableLiveData<String>()
    val imageMessage = MutableLiveData<String>()
    val date = MutableLiveData<String>()

    fun bind(data: ChatItem?) {
        if(data is ChatTextItem) textMessage.value = data.text else if(data is ChatImageItem) imageMessage.value = data.image
        date.value = data?.timestamp?.millisToDateTime()
    }
}