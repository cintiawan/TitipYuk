package xyz.cintiawan.titipyuk.ui.trip.item.vertical

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemTripVerticalBinding
import xyz.cintiawan.titipyuk.model.item.TripItem
import xyz.cintiawan.titipyuk.util.State

class TripVerticalViewAdapter(private val title: String, private val retry: () -> Unit) : PagedListAdapter<TripItem, RecyclerView.ViewHolder>(TripDiffCallback) {
    private val HEADER_VIEW_TYPE = 0
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE  = 2

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            HEADER_VIEW_TYPE -> TripHeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header_vertical, parent, false))
            DATA_VIEW_TYPE -> {
                val binding: ItemTripVerticalBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_trip_vertical, parent, false)
                TripDataVerticalViewHolder(binding)
            }
            FOOTER_VIEW_TYPE -> TripFooterVerticalViewHolder.create(retry, parent)
            else -> throw RuntimeException("Invalid view type $viewType")
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + 1 + if(hasFooter()) 1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is TripHeaderViewHolder -> holder.bind(title)
            is TripDataVerticalViewHolder -> holder.bind(getItem(position - 1), position - 1)
            is TripFooterVerticalViewHolder -> holder.bind(state)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> HEADER_VIEW_TYPE
            (position - 1) < super.getItemCount() -> DATA_VIEW_TYPE
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
        val TripDiffCallback = object : DiffUtil.ItemCallback<TripItem>() {
            override fun areItemsTheSame(oldItem: TripItem, newItem: TripItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TripItem, newItem: TripItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}