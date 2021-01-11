package xyz.cintiawan.titipyuk.ui.chat.item.chat

import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.databinding.ItemChatTextOtherUserBinding
import xyz.cintiawan.titipyuk.model.item.ChatTextItem

class ChatTextOtherUserDataViewHolder(private val binding: ItemChatTextOtherUserBinding) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = ItemChatViewModel()

    fun bind(chat: ChatTextItem?) {
        viewModel.bind(chat)
        binding.viewModel = viewModel
    }
}