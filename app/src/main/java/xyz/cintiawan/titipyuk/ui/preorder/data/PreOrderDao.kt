package xyz.cintiawan.titipyuk.ui.preorder.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import xyz.cintiawan.titipyuk.model.item.PreOrderItem

@Dao
interface PreOrderDao {
    @Query("SELECT * FROM preorders")
    fun getPreOrders(): Single<List<PreOrderItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: PreOrderItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<PreOrderItem>)
}