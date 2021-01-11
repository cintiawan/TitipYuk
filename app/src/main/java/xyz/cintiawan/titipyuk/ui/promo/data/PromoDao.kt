package xyz.cintiawan.titipyuk.ui.promo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import xyz.cintiawan.titipyuk.model.item.PromoItem

@Dao
interface PromoDao {
    @Query("SELECT * FROM promos")
    fun getPreOrders(): Single<List<PromoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: PromoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<PromoItem>)
}