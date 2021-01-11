package xyz.cintiawan.titipyuk.ui.titipan.item

import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.databinding.ItemTitipanBinding
import xyz.cintiawan.titipyuk.model.Titipan

class TitipanDataViewHolder(private val binding: ItemTitipanBinding) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = ItemTitipanViewModel()

    fun bind(titipan: Titipan?) {
        viewModel.bind(titipan)
        binding.viewModel = viewModel
    }
}