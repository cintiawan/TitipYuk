package xyz.cintiawan.titipyuk.ui.negara.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_footer_horizontal.view.*
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.util.State

class NegaraFooterHorizontalViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    fun bind(state: State?) {
//        itemView.progress_bar.visibility = if(state == State.LOADING) View.VISIBLE else View.GONE
        itemView.error.visibility = if(state == State.ERROR) View.VISIBLE else View.GONE
    }

    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): NegaraFooterHorizontalViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_footer_horizontal, parent, false)
            view.btn_retry.setOnClickListener { retry() }

            return NegaraFooterHorizontalViewHolder(view)
        }
    }
}