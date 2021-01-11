package xyz.cintiawan.titipyuk.ui.review.item

import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.databinding.ItemReviewBinding
import xyz.cintiawan.titipyuk.model.item.ReviewItem

class ReviewDataViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = ItemReviewViewModel()

    fun bind(review: ReviewItem?) {
        viewModel.bind(review)
        binding.viewModel = viewModel
    }
}