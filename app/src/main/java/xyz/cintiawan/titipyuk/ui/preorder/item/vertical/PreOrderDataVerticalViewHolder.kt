package xyz.cintiawan.titipyuk.ui.preorder.item.vertical

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.databinding.ItemPreOrderVerticalBinding
import xyz.cintiawan.titipyuk.model.item.PreOrderItem
import xyz.cintiawan.titipyuk.ui.preorder.detail.PreOrderDetailActivity
import xyz.cintiawan.titipyuk.ui.preorder.item.ItemPreOrderViewModel

class PreOrderDataVerticalViewHolder(private val binding: ItemPreOrderVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = ItemPreOrderViewModel()

    fun bind(barang: PreOrderItem?) {
        viewModel.bind(barang)

        binding.viewModel = viewModel
        binding.ltyDetail.setOnClickListener {
            binding.root.context.startActivity(
                Intent(binding.root.context, PreOrderDetailActivity::class.java)
                        .putExtra(PreOrderDetailActivity.PREORDER_ID, barang?.id))
        }
    }
}