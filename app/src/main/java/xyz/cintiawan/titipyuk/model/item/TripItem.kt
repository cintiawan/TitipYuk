package xyz.cintiawan.titipyuk.model.item

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "trips")
data class TripItem(
        @PrimaryKey(autoGenerate = false)
        @SerializedName("id")
        val id: Int,

        @SerializedName("tanggal_berangkat")
        val tanggalBerangkat: Long,

        @SerializedName("tanggal_kembali")
        val tanggalKembali: Long,

        @Embedded(prefix = "user_")
        @SerializedName("user")
        val user: UserItem,

        @Embedded(prefix = "asal_")
        @SerializedName("asal")
        val kotaAsal: CityItem,

        @Embedded(prefix = "tujuan_")
        @SerializedName("tujuan")
        val kotaTujuan: CityItem
)