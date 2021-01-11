package xyz.cintiawan.titipyuk.ui.review.item.paged

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemReviewBinding
import xyz.cintiawan.titipyuk.model.item.ReviewItem
import xyz.cintiawan.titipyuk.ui.review.item.ReviewDataViewHolder
import xyz.cintiawan.titipyuk.ui.review.item.ReviewFooterViewHolder
import xyz.cintiawan.titipyuk.util.State

class ReviewViewPagedAdapter(private val retry: () -> Unit) : PagedListAdapter<ReviewItem, RecyclerView.ViewHolder>(TitipanDiffCallback) {
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == DATA_VIEW_TYPE) {
            val binding: ItemReviewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_review, parent, false)
            ReviewDataViewHolder(binding)
        } else {
            ReviewFooterViewHolder.create(retry, parent)
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + if(hasFooter()) 1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as ReviewDataViewHolder).bind(getItem(position))
        else
            (holder as ReviewFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    private fun hasFooter(): Boolean {
        return (state == State.LOADING || state == State.ERROR) || (super.getItemCount() == 0 && state == State.DONE)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    companion object {
        val TitipanDiffCallback = object : DiffUtil.ItemCallback<ReviewItem>() {
            override fun areItemsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}