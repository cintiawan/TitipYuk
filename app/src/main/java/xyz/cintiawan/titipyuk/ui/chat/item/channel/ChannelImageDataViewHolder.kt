package xyz.cintiawan.titipyuk.ui.chat.item.channel

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.databinding.ItemChatChannelBinding
import xyz.cintiawan.titipyuk.model.item.ChatChannelItem
import xyz.cintiawan.titipyuk.ui.chat.ChatChannelActivity

class ChannelImageDataViewHolder(private val binding: ItemChatChannelBinding) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = ItemChannelViewModel()

    fun bind(channel: ChatChannelItem?) {
        viewModel.bind(channel)

        binding.viewModel = viewModel
        binding.lytChannel.setOnClickListener {
            binding.root.context.startActivity(Intent(binding.root.context, ChatChannelActivity::class.java)
                    .putExtra(ChatChannelActivity.OTHER_USER_ID, channel?.otherUserUid))
        }
    }
}