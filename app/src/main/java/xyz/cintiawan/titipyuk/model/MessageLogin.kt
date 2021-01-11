package xyz.cintiawan.titipyuk.model

import com.google.gson.annotations.SerializedName

data class MessageLogin(
        @SerializedName("token_type")
        val tokenType: String,

        @SerializedName("expires_in")
        val expire: Long,

        @SerializedName("access_token")
        val accessToken: String,

        @SerializedName("refresh_token")
        val refreshToken: String
)