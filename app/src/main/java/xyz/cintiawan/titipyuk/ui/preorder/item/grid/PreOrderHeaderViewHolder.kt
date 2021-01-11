package xyz.cintiawan.titipyuk.ui.preorder.item.grid

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_header_vertical.view.*

class PreOrderHeaderViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val txtTitle = itemView.txt_list_title

    fun bind(title: String) {
        txtTitle.text = title
    }

}