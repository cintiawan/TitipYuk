package xyz.cintiawan.titipyuk.model.item

import com.google.gson.annotations.SerializedName

data class CityOngkirItem(
        @SerializedName("city_id")
        val id: Int,

        @SerializedName("province_id")
        val provinceId: Int,

        @SerializedName("province")
        val province: String,

        @SerializedName("type")
        val type: String,

        @SerializedName("city_name")
        val cityName: String,

        @SerializedName("postal_code")
        val postalCode: String
)