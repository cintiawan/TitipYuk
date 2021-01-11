package xyz.cintiawan.titipyuk.ui.preorder.item.horizontal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemPreOrderBinding
import xyz.cintiawan.titipyuk.model.item.PreOrderItem
import xyz.cintiawan.titipyuk.util.State

class PreOrderHorizontalViewAdapter(private val retry: () -> Unit) : PagedListAdapter<PreOrderItem, RecyclerView.ViewHolder>(PreOrderDiffCallback) {
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == DATA_VIEW_TYPE) {
            val binding: ItemPreOrderBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_pre_order, parent, false)
            PreOrderDataHorizontalViewHolder(binding, parent.context)
        } else {
            PreOrderFooterHorizontalViewHolder.create(retry, parent)
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + (if(hasFooter()) 1 else 0)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as PreOrderDataHorizontalViewHolder).bind(getItem(position), super.getCurrentList()?.toList(), position)
        else
            (holder as PreOrderFooterHorizontalViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() == 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    companion object {
        val PreOrderDiffCallback = object : DiffUtil.ItemCallback<PreOrderItem>() {
            override fun areItemsTheSame(oldItem: PreOrderItem, newItem: PreOrderItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PreOrderItem, newItem: PreOrderItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}