package xyz.cintiawan.titipyuk.ui.user.auth

import android.os.Bundle
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity

class RedirectActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redirect)

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, RedirectFragment())
                .commit()
    }
}
