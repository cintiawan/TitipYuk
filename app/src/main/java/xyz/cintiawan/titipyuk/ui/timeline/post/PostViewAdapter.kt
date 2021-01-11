package xyz.cintiawan.titipyuk.ui.timeline.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemPostBinding
import xyz.cintiawan.titipyuk.model.item.PostItem

class PostViewAdapter : RecyclerView.Adapter<PostViewAdapter.ViewHolder>() {
    private lateinit var posts: List<PostItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPostBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(posts[position])

    fun updateList(data: List<PostItem>) {
        this.posts = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ItemPostViewModel()

        fun bind(post: PostItem) {
            viewModel.bind(post)
            binding.viewModel = viewModel
            binding.lytProfile.setOnClickListener {  }
        }
    }
}