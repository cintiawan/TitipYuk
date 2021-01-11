package xyz.cintiawan.titipyuk.db

import androidx.room.TypeConverter
import xyz.cintiawan.titipyuk.model.Gambar
import xyz.cintiawan.titipyuk.model.item.VarianItem

class CustomTypeConverters {
    @TypeConverter
    fun toListString(value: String): List<String> {
        return value.split("~@~")
    }

    @TypeConverter
    fun toStringString(value: List<String>): String {
        return value.joinToString("~@~")
    }

    @TypeConverter
    fun toListVarian(value: String): List<VarianItem> {
        val result = mutableListOf<VarianItem>()
        var varian: List<String>
        value.split("~@~").forEach {
            varian = it.split("*~*")
            result.add(VarianItem(varian[0].toInt(), varian[1], varian[2].toDouble()))
        }

        return result
    }

    @TypeConverter
    fun toStringVarian(value: List<VarianItem>): String {
        val result = mutableListOf<String>()
        value.forEach {
            result.add("${it.id}*~*${it.nama}*~*${it.harga}")
        }

        return result.joinToString("~@~")
    }

    @TypeConverter
    fun toListGambar(value: String): List<Gambar> {
        val result = mutableListOf<Gambar>()
        value.split("~@~").forEach {
            result.add(Gambar(it))
        }

        return result
    }

    @TypeConverter
    fun toStringGambar(value: List<Gambar>): String {
        val result = mutableListOf<String>()
        value.forEach {
            result.add(it.pathGambar)
        }

        return result.joinToString("~@~")
    }

}