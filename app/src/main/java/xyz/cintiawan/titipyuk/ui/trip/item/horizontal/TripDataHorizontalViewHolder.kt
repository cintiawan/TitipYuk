package xyz.cintiawan.titipyuk.ui.trip.item.horizontal

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.databinding.ItemTripBinding
import xyz.cintiawan.titipyuk.model.item.TripItem
import xyz.cintiawan.titipyuk.ui.trip.detail.TripDetailActivity
import xyz.cintiawan.titipyuk.ui.trip.item.ItemTripViewModel

class TripDataHorizontalViewHolder(private val binding: ItemTripBinding) : RecyclerView.ViewHolder(binding.root) {
    private val viewModel = ItemTripViewModel()

    fun bind(trip: TripItem?, position: Int) {
        viewModel.bind(trip, position)

        binding.viewModel = viewModel
        binding.container.setOnClickListener {
            binding.root.context.startActivity(
                    Intent(binding.root.context, TripDetailActivity::class.java)
                            .putExtra(TripDetailActivity.TRIP_ID, trip?.id))
        }
    }
}