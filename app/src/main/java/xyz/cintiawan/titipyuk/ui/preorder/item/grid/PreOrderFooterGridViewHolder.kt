package xyz.cintiawan.titipyuk.ui.preorder.item.grid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_footer_vertical.view.*
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.util.State

class PreOrderFooterGridViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    fun bind(state: State?) {
        itemView.progress_bar.visibility = if(state == State.LOADING) View.VISIBLE else View.GONE
        itemView.error.visibility = if(state == State.ERROR) View.VISIBLE else View.GONE
    }

    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): PreOrderFooterGridViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_footer_vertical, parent, false)
            view.btn_retry.setOnClickListener { retry() }

            return PreOrderFooterGridViewHolder(view)
        }
    }

}