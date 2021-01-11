package xyz.cintiawan.titipyuk.model.item

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cities")
data class CityItem(
        @PrimaryKey(autoGenerate = false)
        @SerializedName("id")
        val id: Int,

        @SerializedName("nama_kota")
        val name: String,

        @Embedded(prefix = "negara_")
        @SerializedName("negara")
        val negara: NegaraItem
) : Parcelable