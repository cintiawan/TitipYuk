package xyz.cintiawan.titipyuk.ui.request.item.grid

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.databinding.ItemRequestBinding
import xyz.cintiawan.titipyuk.model.item.RequestItem
import xyz.cintiawan.titipyuk.ui.request.item.ItemRequestViewModel
import xyz.cintiawan.titipyuk.ui.request.story.StoryRequestActivity
import java.util.ArrayList

class RequestDataGridViewHolder(private val binding: ItemRequestBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = ItemRequestViewModel()

    fun bind(barang: RequestItem?, barangs: List<RequestItem>?, position: Int) {
        viewModel.bind(barang)
        binding.viewModel = viewModel
        binding.imgBarang.setOnClickListener {
            context.startActivity(Intent(context, StoryRequestActivity::class.java)
                    .putExtra(StoryRequestActivity.OBJECT_ID, position)
                    .putParcelableArrayListExtra(StoryRequestActivity.OBJECT_LIST, barangs as ArrayList<out Parcelable>))
        }
    }
}