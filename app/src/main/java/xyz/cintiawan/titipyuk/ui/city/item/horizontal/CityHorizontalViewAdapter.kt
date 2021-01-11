package xyz.cintiawan.titipyuk.ui.city.item.horizontal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemCityBinding
import xyz.cintiawan.titipyuk.model.item.CityItem
import xyz.cintiawan.titipyuk.ui.city.item.ItemCityViewModel
import xyz.cintiawan.titipyuk.util.State

class CityHorizontalViewAdapter(private val retry: () -> Unit) : PagedListAdapter<CityItem, RecyclerView.ViewHolder>(CityDiffCallback) {
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == DATA_VIEW_TYPE) {
            val binding: ItemCityBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_city, parent, false)
            ViewHolder(binding)
        } else {
            CityFooterHorizontalViewHolder.create(retry, parent)
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + if(hasFooter()) 1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as ViewHolder).bind(getItem(position))
        else
            (holder as CityFooterHorizontalViewHolder).bind(state)
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
        val CityDiffCallback = object : DiffUtil.ItemCallback<CityItem>() {
            override fun areItemsTheSame(oldItem: CityItem, newItem: CityItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CityItem, newItem: CityItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ViewHolder(private val binding: ItemCityBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ItemCityViewModel()

        fun bind(city: CityItem?) {
            viewModel.bind(city)
            binding.viewModel = viewModel
        }
    }

}