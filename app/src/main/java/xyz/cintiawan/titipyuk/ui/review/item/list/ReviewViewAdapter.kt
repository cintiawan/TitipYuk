package xyz.cintiawan.titipyuk.ui.review.item.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemReviewBinding
import xyz.cintiawan.titipyuk.model.item.ReviewItem
import xyz.cintiawan.titipyuk.ui.review.item.ReviewDataViewHolder
import xyz.cintiawan.titipyuk.ui.review.item.ReviewFooterViewHolder
import xyz.cintiawan.titipyuk.util.State

class ReviewViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var reviews = listOf<ReviewItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            DATA_VIEW_TYPE -> {
                val binding: ItemReviewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_review, parent, false)
                ReviewDataViewHolder(binding)
            }
            FOOTER_VIEW_TYPE -> ReviewFooterViewHolder.create({ }, parent)
            else -> throw RuntimeException("Invalid view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ReviewDataViewHolder -> holder.bind(reviews[position])
            is ReviewFooterViewHolder -> holder.bind(State.DONE)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < reviews.size -> DATA_VIEW_TYPE
            else -> FOOTER_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int = reviews.size + if(hasFooter()) 1 else 0

    private fun hasFooter(): Boolean = reviews.isEmpty()

    fun updateList(data: List<ReviewItem>) {
        this.reviews = data
        notifyDataSetChanged()
    }

}