package xyz.cintiawan.titipyuk.ui.negara.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import xyz.cintiawan.titipyuk.model.item.NegaraItem

@Dao
interface NegaraDao {
    @Query("SELECT * FROM negaras")
    fun getNegaras(): Single<List<NegaraItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: NegaraItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<NegaraItem>)
}