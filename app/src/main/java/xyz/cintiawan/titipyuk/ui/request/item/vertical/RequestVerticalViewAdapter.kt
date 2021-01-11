package xyz.cintiawan.titipyuk.ui.request.item.vertical

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemRequestVerticalBinding
import xyz.cintiawan.titipyuk.model.item.RequestItem
import xyz.cintiawan.titipyuk.util.State

class RequestVerticalViewAdapter(private val retry: () -> Unit) : PagedListAdapter<RequestItem, RecyclerView.ViewHolder>(RequestDiffCallback) {
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            DATA_VIEW_TYPE -> {
                val binding: ItemRequestVerticalBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_request_vertical, parent, false)
                RequestDataVerticalViewHolder(binding)
            }
            FOOTER_VIEW_TYPE -> RequestFooterVerticalViewHolder.create(retry, parent)
            else -> throw RuntimeException("Invalid view type $viewType")
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + if(hasFooter()) 1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is RequestDataVerticalViewHolder -> holder.bind(getItem(position))
            is RequestFooterVerticalViewHolder -> holder.bind(state)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < super.getItemCount() -> DATA_VIEW_TYPE
            else -> FOOTER_VIEW_TYPE
        }
    }

    private fun hasFooter(): Boolean {
        return (state == State.LOADING || state == State.ERROR) || (super.getItemCount() == 0 && state == State.DONE)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    companion object {
        val RequestDiffCallback = object : DiffUtil.ItemCallback<RequestItem>() {
            override fun areItemsTheSame(oldItem: RequestItem, newItem: RequestItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RequestItem, newItem: RequestItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}