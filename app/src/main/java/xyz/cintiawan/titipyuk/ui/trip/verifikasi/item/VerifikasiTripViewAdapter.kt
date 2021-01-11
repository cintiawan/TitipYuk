package xyz.cintiawan.titipyuk.ui.trip.verifikasi.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.databinding.ItemVerifikasiTripBinding
import xyz.cintiawan.titipyuk.model.item.VerifikasiTripItem
import xyz.cintiawan.titipyuk.util.State

class VerifikasiTripViewAdapter(private val retry: () -> Unit, private val onRespond: (Int, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    private var data = listOf<VerifikasiTripItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            DATA_VIEW_TYPE -> {
                val binding: ItemVerifikasiTripBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_verifikasi_trip, parent, false)
                VerifikasiTripDataViewHolder(binding, onRespond)
            }
            FOOTER_VIEW_TYPE -> VerifikasiTripFooterViewHolder.create(retry, parent)
            else -> throw RuntimeException("Invalid view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is VerifikasiTripDataViewHolder -> holder.bind(data[position])
            is VerifikasiTripFooterViewHolder -> holder.bind(state)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < data.size -> DATA_VIEW_TYPE
            else -> FOOTER_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int = data.size + if(hasFooter()) 1 else 0

    private fun hasFooter(): Boolean = (state == State.LOADING || state == State.ERROR) || (data.count() == 0 && state == State.DONE)

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(itemCount)
    }

    fun updateList(data: List<VerifikasiTripItem>) {
        this.data = data
        notifyDataSetChanged()
    }

}