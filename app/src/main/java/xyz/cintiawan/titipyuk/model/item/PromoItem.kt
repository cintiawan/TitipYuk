package xyz.cintiawan.titipyuk.model.item

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "promos")
data class PromoItem(
        @PrimaryKey(autoGenerate = false)
        @SerializedName("id")
        val id: Int,

        @SerializedName("nama")
        val nama: String,

        @SerializedName("banner_path")
        val bannerPath: String,

        @SerializedName("deskripsi")
        val deskripsi: String
)