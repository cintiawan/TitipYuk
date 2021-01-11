package xyz.cintiawan.titipyuk.ui.alamat.item.list

import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.databinding.ItemAlamatBinding
import xyz.cintiawan.titipyuk.model.item.AlamatItem
import xyz.cintiawan.titipyuk.ui.alamat.item.ItemAlamatViewModel

class AlamatDataViewHolder(private val binding: ItemAlamatBinding) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = ItemAlamatViewModel()

    fun bind(alamat: AlamatItem?) {
        viewModel.bind(alamat)
        binding.viewModel = viewModel
    }
}