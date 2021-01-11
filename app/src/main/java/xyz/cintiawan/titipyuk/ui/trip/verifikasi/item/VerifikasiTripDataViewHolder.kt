package xyz.cintiawan.titipyuk.ui.trip.verifikasi.item

import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.databinding.ItemVerifikasiTripBinding
import xyz.cintiawan.titipyuk.model.item.VerifikasiTripItem

class VerifikasiTripDataViewHolder(private val binding: ItemVerifikasiTripBinding, private val onRespond: (Int, Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = ItemVerifikasiTripViewModel()

    fun bind(data: VerifikasiTripItem?) {
        viewModel.bind(data)

        binding.btnTerima.setOnClickListener { data?.id?.let { it1 -> onRespond(it1, 1) } }
        binding.btnTolak.setOnClickListener { data?.id?.let { it1 -> onRespond(it1, 0) } }
        binding.viewModel = viewModel
    }
}