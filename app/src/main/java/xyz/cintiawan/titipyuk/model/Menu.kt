package xyz.cintiawan.titipyuk.model

import android.graphics.drawable.Drawable

data class Menu(
        val image: Drawable?,
        val title: String,
        val startActivity: Class<*>
)