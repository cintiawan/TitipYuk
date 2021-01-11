package xyz.cintiawan.titipyuk.ui.request.item.grid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemRequestBinding
import xyz.cintiawan.titipyuk.model.item.RequestItem
import xyz.cintiawan.titipyuk.util.State

class RequestGridViewAdapter(private val title: String, private val retry: () -> Unit) : PagedListAdapter<RequestItem, RecyclerView.ViewHolder>(RequestDiffCallback) {
    private val HEADER_VIEW_TYPE = 0
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE  = 2

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            HEADER_VIEW_TYPE -> RequestHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header_vertical, parent, false))
            DATA_VIEW_TYPE -> {
                val binding: ItemRequestBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_request, parent, false)
                RequestDataGridViewHolder(binding, parent.context)
            }
            FOOTER_VIEW_TYPE -> RequestFooterGridViewHolder.create(retry, parent)
            else -> throw RuntimeException("Invalid view type $viewType")
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + 1 + (if(hasFooter()) 1 else 0)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is RequestHeaderViewHolder -> holder.bind(title)
            is RequestDataGridViewHolder -> holder.bind(getItem(position - 1), super.getCurrentList()?.toList(), position)
            is RequestFooterGridViewHolder -> holder.bind(state)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> HEADER_VIEW_TYPE
            (position - 1) < super.getItemCount() -> DATA_VIEW_TYPE
            else -> FOOTER_VIEW_TYPE
        }
    }

    fun hasFooter(): Boolean {
        return (state == State.LOADING || state == State.ERROR)
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