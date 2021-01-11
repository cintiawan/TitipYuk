package xyz.cintiawan.titipyuk.ui.preorder.item.horizontal

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.databinding.ItemPreOrderBinding
import xyz.cintiawan.titipyuk.model.item.PreOrderItem
import xyz.cintiawan.titipyuk.ui.preorder.item.ItemPreOrderViewModel
import xyz.cintiawan.titipyuk.ui.preorder.story.StoryPreOrderActivity
import java.util.ArrayList

class PreOrderDataHorizontalViewHolder(private val binding: ItemPreOrderBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = ItemPreOrderViewModel()

    fun bind(barang: PreOrderItem?, barangs: List<PreOrderItem>?, position: Int) {
        viewModel.bind(barang)

        binding.viewModel = viewModel
        binding.imgBarang.setOnClickListener {
            context.startActivity(Intent(context, StoryPreOrderActivity::class.java)
                    .putExtra(StoryPreOrderActivity.OBJECT_ID, position)
                    .putParcelableArrayListExtra(StoryPreOrderActivity.OBJECT_LIST, barangs as ArrayList<out Parcelable>))
        }
    }

}