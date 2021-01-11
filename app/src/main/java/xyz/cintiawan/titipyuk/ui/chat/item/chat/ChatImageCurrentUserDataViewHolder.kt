package xyz.cintiawan.titipyuk.ui.chat.item.chat

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.databinding.ItemChatImageCurrentUserBinding
import xyz.cintiawan.titipyuk.model.item.ChatImageItem
import xyz.cintiawan.titipyuk.ui.slider.ImageFullActivity

class ChatImageCurrentUserDataViewHolder(private val binding: ItemChatImageCurrentUserBinding) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = ItemChatViewModel()

    fun bind(chat: ChatImageItem?) {
        viewModel.bind(chat)

        binding.viewModel = viewModel
        binding.imgMessage.setOnClickListener {
            binding.root.context.startActivity(Intent(binding.root.context, ImageFullActivity::class.java)
                    .putExtra(ImageFullActivity.SOURCE, chat?.image)
                    .putExtra(ImageFullActivity.IS_FILE, false))
        }
    }
}