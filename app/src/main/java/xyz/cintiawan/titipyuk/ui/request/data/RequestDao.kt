package xyz.cintiawan.titipyuk.ui.request.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import xyz.cintiawan.titipyuk.model.item.RequestItem

@Dao
interface RequestDao {
    @Query("SELECT * FROM requests")
    fun getRequests(): Single<List<RequestItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: RequestItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<RequestItem>)
}