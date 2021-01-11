package xyz.cintiawan.titipyuk.ui.chat.item.channel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemChatChannelBinding
import xyz.cintiawan.titipyuk.model.item.ChatChannelItem

class ChannelViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data = mutableListOf<ChatChannelItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemChatChannelBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_chat_channel, parent, false)
        return ChannelImageDataViewHolder(binding)
    }

    override fun getItemCount(): Int = this.data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChannelImageDataViewHolder).bind(data[position])
    }

    fun setData(data: List<ChatChannelItem>) {
        val diff = DiffUtil.calculateDiff(ChannelDiffCallback(this.data, data))
        diff.dispatchUpdatesTo(this)

        this.data.clear()
        this.data.addAll(data)
    }

}