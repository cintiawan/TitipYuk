package xyz.cintiawan.titipyuk.ui.trip.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import xyz.cintiawan.titipyuk.model.item.TripItem

@Dao
interface TripDao {
    @Query("SELECT * FROM trips")
    fun getTrips(): Single<List<TripItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: TripItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<TripItem>)
}