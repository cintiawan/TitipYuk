package xyz.cintiawan.titipyuk.util

import android.content.SharedPreferences

object SPUtil {
    fun hasAccessToken(sp: SharedPreferences): Boolean {
        return !sp.getString(Constants.SP_ACCESS_TOKEN, "").isNullOrEmpty()
    }

    fun getAccessToken(sp: SharedPreferences): String? {
        return sp.getString(Constants.SP_ACCESS_TOKEN, "")
    }

    fun setAccessToken(sp: SharedPreferences, token: String) {
        sp.edit().putString(Constants.SP_ACCESS_TOKEN, token).apply()
    }

    fun removeAccessToken(sp: SharedPreferences) {
        sp.edit().remove(Constants.SP_ACCESS_TOKEN).apply()
    }

}