package xyz.cintiawan.titipyuk.ui.trip.item

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import xyz.cintiawan.titipyuk.base.BaseViewModel
import xyz.cintiawan.titipyuk.model.item.TripItem
import xyz.cintiawan.titipyuk.util.millisToDate
import xyz.cintiawan.titipyuk.util.toHtml

class ItemTripViewModel : BaseViewModel() {
    private val tripId = MutableLiveData<Int>()
    val color = MutableLiveData<Int>()
    val userImage = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val tripName = MutableLiveData<String>()
    val tripDepart = MutableLiveData<String>()
    val tripReturn = MutableLiveData<String>()

    private val colors = arrayOf(
            Color.rgb(246, 76, 115),
            Color.rgb(32, 210, 187),
            Color.rgb(51, 149, 255),
            Color.rgb(200, 115, 244)
    )

    fun bind(trip: TripItem?, position: Int) {
        color.value = colors[position % colors.size]
        tripId.value = trip?.id
        userImage.value = trip?.user?.image
        userName.value = trip?.user?.name
        tripName.value = "${trip?.kotaAsal?.name} ${"&#9658;".toHtml()} ${trip?.kotaTujuan?.name}"
        tripDepart.value = "Keberangkatan: ${trip?.tanggalBerangkat?.millisToDate()}"
        tripReturn.value = "Kembali: ${trip?.tanggalKembali?.millisToDate()}"
    }

}