package xyz.cintiawan.titipyuk.ui.trip.item.vertical

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_header_vertical.view.*
import xyz.cintiawan.titipyuk.util.gone

class TripHeaderViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val txtTitle = itemView.txt_list_title

    fun bind(title: String) {
        if(title.isEmpty()) itemView.gone() else txtTitle.text = title
    }

}