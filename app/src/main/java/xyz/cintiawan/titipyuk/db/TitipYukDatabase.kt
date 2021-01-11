package xyz.cintiawan.titipyuk.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import xyz.cintiawan.titipyuk.model.item.*
import xyz.cintiawan.titipyuk.ui.city.data.CityDao
import xyz.cintiawan.titipyuk.ui.negara.data.NegaraDao
import xyz.cintiawan.titipyuk.ui.preorder.data.PreOrderDao
import xyz.cintiawan.titipyuk.ui.promo.data.PromoDao
import xyz.cintiawan.titipyuk.ui.request.data.RequestDao
import xyz.cintiawan.titipyuk.ui.timeline.data.PostDao
import xyz.cintiawan.titipyuk.ui.trip.data.TripDao

@Database(entities = [
    PromoItem::class,
    PreOrderItem::class,
    RequestItem::class,
    TripItem::class,
    CityItem::class,
    NegaraItem::class,
    PostItem::class], version = 1)
@TypeConverters(CustomTypeConverters::class)
abstract class TitipYukDatabase : RoomDatabase() {
    abstract fun promoDao(): PromoDao
    abstract fun preOrderDao(): PreOrderDao
    abstract fun requestDao(): RequestDao
    abstract fun tripDao(): TripDao
    abstract fun negaraDao(): NegaraDao
    abstract fun cityDao(): CityDao
    abstract fun postDao(): PostDao
}