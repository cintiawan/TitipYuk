package xyz.cintiawan.titipyuk.ui.request.item.vertical

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.databinding.ItemRequestVerticalBinding
import xyz.cintiawan.titipyuk.model.item.RequestItem
import xyz.cintiawan.titipyuk.ui.request.detail.RequestDetailActivity
import xyz.cintiawan.titipyuk.ui.request.item.ItemRequestViewModel

class RequestDataVerticalViewHolder(private val binding: ItemRequestVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = ItemRequestViewModel()

    fun bind(barang: RequestItem?) {
        viewModel.bind(barang)
        binding.viewModel = viewModel
        binding.lytDetail.setOnClickListener {
            binding.root.context.startActivity(
                Intent(binding.root.context, RequestDetailActivity::class.java)
                        .putExtra(RequestDetailActivity.REQUEST_ID, barang?.id))
        }
    }
}