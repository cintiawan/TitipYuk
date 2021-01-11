package xyz.cintiawan.titipyuk.ui.trip.item.vertical

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.databinding.ItemTripVerticalBinding
import xyz.cintiawan.titipyuk.model.item.TripItem
import xyz.cintiawan.titipyuk.ui.trip.detail.TripDetailActivity
import xyz.cintiawan.titipyuk.ui.trip.item.ItemTripViewModel

class TripDataVerticalViewHolder(private val binding: ItemTripVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = ItemTripViewModel()

    fun bind(trip: TripItem?, position: Int) {
        viewModel.bind(trip, position)

        binding.viewModel = viewModel
        binding.lytDetail.setOnClickListener {
            binding.root.context.startActivity(
                    Intent(binding.root.context, TripDetailActivity::class.java)
                            .putExtra(TripDetailActivity.TRIP_ID, trip?.id))
        }
    }
}