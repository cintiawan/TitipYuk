package xyz.cintiawan.titipyuk.model.item

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "negaras")
data class NegaraItem(
        @PrimaryKey(autoGenerate = false)
        @SerializedName("nama_negara")
        val name: String,

        @SerializedName("bendera_path")
        val flag: String,

        @SerializedName("wallpaper")
        val image: String
) : Parcelable