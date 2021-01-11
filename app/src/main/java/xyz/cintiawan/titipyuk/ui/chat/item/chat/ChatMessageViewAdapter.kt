package xyz.cintiawan.titipyuk.ui.chat.item.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemChatImageCurrentUserBinding
import xyz.cintiawan.titipyuk.databinding.ItemChatImageOtherUserBinding
import xyz.cintiawan.titipyuk.databinding.ItemChatTextCurrentUserBinding
import xyz.cintiawan.titipyuk.databinding.ItemChatTextOtherUserBinding
import xyz.cintiawan.titipyuk.model.item.ChatImageItem
import xyz.cintiawan.titipyuk.model.item.ChatItem
import xyz.cintiawan.titipyuk.model.item.ChatTextItem
import xyz.cintiawan.titipyuk.util.MessageType

class ChatMessageViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TEXT_CURRENT_USER_VIEW_TYPE = 1
    private val IMAGE_CURRENT_USER_VIEW_TYPE = 2
    private val TEXT_OTHER_USER_VIEW_TYPE = 3
    private val IMAGE_OTHER_USER_VIEW_TYPE = 4

    private val data = mutableListOf<ChatItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TEXT_CURRENT_USER_VIEW_TYPE -> {
                val binding: ItemChatTextCurrentUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_chat_text_current_user, parent, false)
                ChatTextCurrentUserDataViewHolder(binding)
            }
            TEXT_OTHER_USER_VIEW_TYPE -> {
                val binding: ItemChatTextOtherUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_chat_text_other_user, parent, false)
                ChatTextOtherUserDataViewHolder(binding)
            }
            IMAGE_CURRENT_USER_VIEW_TYPE -> {
                val binding: ItemChatImageCurrentUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_chat_image_current_user, parent, false)
                ChatImageCurrentUserDataViewHolder(binding)
            }
            else -> {
                val binding: ItemChatImageOtherUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_chat_image_other_user, parent, false)
                ChatImageOtherUserDataViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = this.data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            TEXT_CURRENT_USER_VIEW_TYPE -> (holder as ChatTextCurrentUserDataViewHolder).bind(data[position] as ChatTextItem)
            TEXT_OTHER_USER_VIEW_TYPE -> (holder as ChatTextOtherUserDataViewHolder).bind(data[position] as ChatTextItem)
            IMAGE_CURRENT_USER_VIEW_TYPE -> (holder as ChatImageCurrentUserDataViewHolder).bind(data[position] as ChatImageItem)
            IMAGE_OTHER_USER_VIEW_TYPE -> (holder as ChatImageOtherUserDataViewHolder).bind(data[position] as ChatImageItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = data[position]
        return if(item.type == MessageType.IMAGE) {
            if(item.senderId == FirebaseAuth.getInstance().currentUser?.uid) IMAGE_CURRENT_USER_VIEW_TYPE
            else IMAGE_OTHER_USER_VIEW_TYPE
        } else {
            if(item.senderId == FirebaseAuth.getInstance().currentUser?.uid) TEXT_CURRENT_USER_VIEW_TYPE
            else TEXT_OTHER_USER_VIEW_TYPE
        }
    }

    fun setData(data: List<ChatItem>) {
        val diff = DiffUtil.calculateDiff(ChatDiffCallback(this.data, data))
        diff.dispatchUpdatesTo(this)

        this.data.clear()
        this.data.addAll(data)
    }

}