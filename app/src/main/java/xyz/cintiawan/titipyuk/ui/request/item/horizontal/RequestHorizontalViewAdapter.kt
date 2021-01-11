package xyz.cintiawan.titipyuk.ui.request.item.horizontal

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

class RequestHorizontalViewAdapter(private val retry: () -> Unit) : PagedListAdapter<RequestItem, RecyclerView.ViewHolder>(RequestDiffCallback) {
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == DATA_VIEW_TYPE) {
            val binding: ItemRequestBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_request, parent, false)
            RequestDataHorizontalViewHolder(binding, parent.context)
        } else {
            RequestFooterHorizontalViewHolder.create(retry, parent)
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + (if(hasFooter()) 1 else 0)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as RequestDataHorizontalViewHolder).bind(getItem(position), super.getCurrentList()?.toList(), position)
        else
            (holder as RequestFooterHorizontalViewHolder).bind(state)
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