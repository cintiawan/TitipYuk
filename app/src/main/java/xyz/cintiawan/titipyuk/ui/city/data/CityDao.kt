package xyz.cintiawan.titipyuk.ui.city.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import xyz.cintiawan.titipyuk.model.item.CityItem

@Dao
interface CityDao {
    @Query("SELECT * FROM cities")
    fun getCities(): Single<List<CityItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: CityItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<CityItem>)
}