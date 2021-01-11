package xyz.cintiawan.titipyuk.model

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import xyz.cintiawan.titipyuk.model.detail.BarangDetail
import xyz.cintiawan.titipyuk.model.detail.UserDetail
import xyz.cintiawan.titipyuk.model.item.CityItem

data class Titipan(
        @SerializedName("id")
        val id: Int,

        @SerializedName("kode")
        val kode: String,

        @SerializedName("barangs")
        val barangs: List<BarangDetail>,

        @Embedded(prefix = "user_")
        @SerializedName("user_penitip")
        val penitip: UserDetail,

        @Embedded(prefix = "user_")
        @SerializedName("user_shopper")
        val shopper: UserDetail,

        @Embedded(prefix = "from_")
        @SerializedName("dibeliDari")
        val from: CityItem,

        @Embedded(prefix = "sent_from_")
        @SerializedName("sent_from")
        val sentFrom: CityItem,

        @Embedded(prefix = "sent_to_")
        @SerializedName("sent_to")
        val sentTo: CityItem,

        @SerializedName("status")
        val status: String,

        @SerializedName("kirim_price")
        val kirimPrice: Double,

        @SerializedName("grand_total")
        val grandTotal: Double
)