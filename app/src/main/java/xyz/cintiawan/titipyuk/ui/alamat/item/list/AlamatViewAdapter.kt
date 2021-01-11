package xyz.cintiawan.titipyuk.ui.alamat.item.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemAlamatBinding
import xyz.cintiawan.titipyuk.model.item.AlamatItem
import xyz.cintiawan.titipyuk.ui.alamat.item.AlamatFooterViewHolder
import xyz.cintiawan.titipyuk.util.State

class AlamatViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    private var alamats = listOf<AlamatItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            DATA_VIEW_TYPE -> {
                val binding: ItemAlamatBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_alamat, parent, false)
                AlamatDataViewHolder(binding)
            }
            FOOTER_VIEW_TYPE -> AlamatFooterViewHolder.create({ }, parent)
            else -> throw RuntimeException("Invalid view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is AlamatDataViewHolder -> holder.bind(alamats[position])
            is AlamatFooterViewHolder -> holder.bind(state)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < alamats.size -> DATA_VIEW_TYPE
            else -> FOOTER_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int = alamats.size + if(hasFooter()) 1 else 0

    private fun hasFooter(): Boolean = (state == State.LOADING || state == State.ERROR) || (alamats.count() == 0 && state == State.DONE)

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(itemCount)
    }

    fun updateList(data: List<AlamatItem>) {
        this.alamats = data
        notifyDataSetChanged()
    }

}