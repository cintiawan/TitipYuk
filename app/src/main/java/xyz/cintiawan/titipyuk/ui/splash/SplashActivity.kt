package xyz.cintiawan.titipyuk.ui.splash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.ui.intro.IntroActivity
import xyz.cintiawan.titipyuk.ui.main.MainActivity
import xyz.cintiawan.titipyuk.util.Constants
import javax.inject.Inject

class SplashActivity : BaseActivity() {
    @Inject lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()

        if(!sharedPreferences.getBoolean(Constants.SP_ONBOARD, false)) {
            startActivity(Intent(this, IntroActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}
