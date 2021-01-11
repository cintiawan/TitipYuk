package xyz.cintiawan.titipyuk.ui.titipan.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemTitipanBinding
import xyz.cintiawan.titipyuk.model.Titipan
import xyz.cintiawan.titipyuk.util.State

class TitipanViewAdapter(private val retry: () -> Unit) : PagedListAdapter<Titipan, RecyclerView.ViewHolder>(TitipanDiffCallback) {
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == DATA_VIEW_TYPE) {
            val binding: ItemTitipanBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_titipan, parent, false)
            TitipanDataViewHolder(binding)
        } else {
            TitipanFooterViewHolder.create(retry, parent)
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + if(hasFooter()) 1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as TitipanDataViewHolder).bind(getItem(position))
        else
            (holder as TitipanFooterViewHolder).bind(state)
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
        val TitipanDiffCallback = object : DiffUtil.ItemCallback<Titipan>() {
            override fun areItemsTheSame(oldItem: Titipan, newItem: Titipan): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Titipan, newItem: Titipan): Boolean {
                return oldItem == newItem
            }
        }
    }

}