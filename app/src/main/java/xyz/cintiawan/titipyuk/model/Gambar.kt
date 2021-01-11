package xyz.cintiawan.titipyuk.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Gambar(@SerializedName("path_gambar") val pathGambar: String) : Parcelable