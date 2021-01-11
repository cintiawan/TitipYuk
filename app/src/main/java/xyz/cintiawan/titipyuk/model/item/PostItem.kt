package xyz.cintiawan.titipyuk.model.item

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "posts")
data class PostItem(
        @PrimaryKey(autoGenerate = false)
        @SerializedName("id")
        val id: Int,

        @SerializedName("type")
        val type: String,

        @SerializedName("likes")
        val likes: Int,

        @SerializedName("comments")
        val comments: Int,

        @SerializedName("shares")
        val shares: Int,

        @Embedded(prefix = "user_")
        @SerializedName("user")
        val user: UserItem
)