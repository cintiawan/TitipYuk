package xyz.cintiawan.titipyuk.ui.trip.item.horizontal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemTripBinding
import xyz.cintiawan.titipyuk.model.item.TripItem
import xyz.cintiawan.titipyuk.util.State

class TripHorizontalViewAdapter(private val retry: () -> Unit) : PagedListAdapter<TripItem, RecyclerView.ViewHolder>(TripDiffCallback) {
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == DATA_VIEW_TYPE) {
            val binding: ItemTripBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_trip, parent, false)
            TripDataHorizontalViewHolder(binding)
        } else {
            TripFooterHorizontalViewHolder.create(retry, parent)
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + if(hasFooter()) 1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as TripDataHorizontalViewHolder).bind(getItem(position), position)
        else
            (holder as TripFooterHorizontalViewHolder).bind(state)
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