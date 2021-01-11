package xyz.cintiawan.titipyuk.ui.alamat.item.selectable

import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.databinding.ItemAlamatSelectBinding
import xyz.cintiawan.titipyuk.model.item.AlamatItem
import xyz.cintiawan.titipyuk.ui.alamat.item.ItemAlamatViewModel

class AlamatSelectDataViewHolder(private val binding: ItemAlamatSelectBinding, private val onSelect: (AlamatItem) -> Unit) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = ItemAlamatViewModel()

    fun bind(alamat: AlamatItem?) {
        viewModel.bind(alamat)
        binding.lytAlamat.setOnClickListener { alamat?.let { it1 -> onSelect(it1) } }
        binding.viewModel = viewModel
    }
}