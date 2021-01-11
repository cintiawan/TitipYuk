package xyz.cintiawan.titipyuk.model.item

import android.os.Parcelable
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import xyz.cintiawan.titipyuk.model.Gambar
import xyz.cintiawan.titipyuk.model.Kategori

@Parcelize
data class BarangItem(
        @SerializedName("nama")
        val nama: String,

        @SerializedName("varian")
        val varian: List<VarianItem>,

        @SerializedName("gambar")
        val gambars: List<Gambar>
) : Parcelable