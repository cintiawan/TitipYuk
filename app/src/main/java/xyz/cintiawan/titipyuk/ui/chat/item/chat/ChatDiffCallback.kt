package xyz.cintiawan.titipyuk.ui.chat.item.chat

import androidx.recyclerview.widget.DiffUtil
import xyz.cintiawan.titipyuk.model.item.ChatImageItem
import xyz.cintiawan.titipyuk.model.item.ChatItem
import xyz.cintiawan.titipyuk.model.item.ChatTextItem

class ChatDiffCallback(private val newItem: List<ChatItem>,private val oldItem: List<ChatItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItem.size
    }

    override fun getNewListSize(): Int {
        return newItem.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return ((oldItem[oldItemPosition] is ChatTextItem && newItem[newItemPosition] is ChatTextItem)
                || (oldItem[oldItemPosition] is ChatImageItem && newItem[newItemPosition] is ChatImageItem))
                && oldItem[oldItemPosition] == newItem[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return ((oldItem[oldItemPosition] is ChatTextItem && newItem[newItemPosition] is ChatTextItem)
                || (oldItem[oldItemPosition] is ChatImageItem && newItem[newItemPosition] is ChatImageItem))
                && oldItem[oldItemPosition] == newItem[newItemPosition]
    }
}