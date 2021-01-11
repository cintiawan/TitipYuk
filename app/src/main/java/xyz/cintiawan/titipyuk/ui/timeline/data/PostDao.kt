package xyz.cintiawan.titipyuk.ui.timeline.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import xyz.cintiawan.titipyuk.model.item.PostItem

@Dao
interface PostDao {
    @Query("SELECT * FROM posts")
    fun getPreOrders(): Single<List<PostItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: PostItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<PostItem>)
}