package xyz.cintiawan.titipyuk.ui.negara.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemNegaraBinding
import xyz.cintiawan.titipyuk.model.item.NegaraItem
import xyz.cintiawan.titipyuk.util.State

class NegaraHorizontalViewAdapter(private val retry: () -> Unit) : PagedListAdapter<NegaraItem, RecyclerView.ViewHolder>(CityDiffCallback) {
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == DATA_VIEW_TYPE) {
            val binding: ItemNegaraBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_negara, parent, false)
            ViewHolder(binding)
        } else {
            NegaraFooterHorizontalViewHolder.create(retry, parent)
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + if(hasFooter()) 1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as ViewHolder).bind(getItem(position))
        else
            (holder as NegaraFooterHorizontalViewHolder).bind(state)
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
        val CityDiffCallback = object : DiffUtil.ItemCallback<NegaraItem>() {
            override fun areItemsTheSame(oldItem: NegaraItem, newItem: NegaraItem): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: NegaraItem, newItem: NegaraItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(private val binding: ItemNegaraBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ItemNegaraViewModel()

        fun bind(negara: NegaraItem?) {
            viewModel.bind(negara)
            binding.viewModel = viewModel
        }
    }

}