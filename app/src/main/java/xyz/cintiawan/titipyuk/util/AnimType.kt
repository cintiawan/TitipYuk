package xyz.cintiawan.titipyuk.util

import xyz.cintiawan.titipyuk.R

enum class AnimType {
    SLIDE,
    FADE,
    MIX;

    fun getAnimPair(): Pair<Int, Int> {
        return when(this) {
            SLIDE -> Pair(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            FADE -> Pair(android.R.anim.fade_in, android.R.anim.fade_out)
            MIX -> Pair(android.R.anim.fade_in, R.anim.slide_right)
        }
    }
}