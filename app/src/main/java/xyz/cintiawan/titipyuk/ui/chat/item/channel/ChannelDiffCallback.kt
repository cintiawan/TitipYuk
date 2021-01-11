package xyz.cintiawan.titipyuk.ui.chat.item.channel

import androidx.recyclerview.widget.DiffUtil
import xyz.cintiawan.titipyuk.model.item.ChatChannelItem

class ChannelDiffCallback(private val newItem: List<ChatChannelItem>,private val oldItem: List<ChatChannelItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItem.size
    }

    override fun getNewListSize(): Int {
        return newItem.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItem[oldItemPosition].channelId == newItem[newItemPosition].channelId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItem[oldItemPosition] == newItem[newItemPosition]
    }
}