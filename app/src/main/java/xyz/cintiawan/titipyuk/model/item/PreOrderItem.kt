package xyz.cintiawan.titipyuk.model.item

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "preorders")
data class PreOrderItem(
        @PrimaryKey(autoGenerate = false)
        @SerializedName("id")
        val id: Int,

        @Embedded(prefix = "detail_")
        @SerializedName("detail")
        val barang: BarangItem,

        @Embedded(prefix = "user_")
        @SerializedName("user")
        val shopper: UserItem,

        @Embedded(prefix = "dibelidari_")
        @SerializedName("dibelidari")
        val dibeliDari: CityItem
) : Parcelable