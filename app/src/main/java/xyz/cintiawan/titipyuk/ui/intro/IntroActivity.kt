package xyz.cintiawan.titipyuk.ui.intro

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_intro.*
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.ui.main.MainActivity
import xyz.cintiawan.titipyuk.util.Constants
import xyz.cintiawan.titipyuk.util.gone
import xyz.cintiawan.titipyuk.util.visible
import javax.inject.Inject

class IntroActivity : BaseActivity() {
    @Inject lateinit var sp: SharedPreferences

    // Adapter
    private lateinit var adapter: IntroViewPagerAdapter

    private lateinit var colorsActive: IntArray
    private lateinit var colorsInactive: IntArray
    private val dots: MutableList<TextView> = mutableListOf()
    private val pages = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()

        // Change status bar color
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }

        setContentView(R.layout.activity_intro)

        adapter = IntroViewPagerAdapter()
        adapter.addLayout(R.layout.intro_slide1)
        adapter.addLayout(R.layout.intro_slide2)
        adapter.addLayout(R.layout.intro_slide3)
        adapter.addLayout(R.layout.intro_slide4)

        colorsActive = resources.getIntArray(R.array.array_dot_active)
        colorsInactive = resources.getIntArray(R.array.array_dot_inactive)

        viewpager.adapter = adapter
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                addBottomDots(position)

                if(position == pages - 1) {
                    btn_next.text = "MENGERTI"
                    btn_skip.gone()
                } else {
                    btn_next.text = "NEXT"
                    btn_skip.visible()
                }
            }
            override fun onPageScrollStateChanged(state: Int) { }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }
        })
        btn_next.setOnClickListener { nextPage(getItem(+1)) }
        btn_skip.setOnClickListener { startMainActivity() }
    }

    private fun getItem(i: Int): Int {
        return viewpager.currentItem + i
    }

    private fun addBottomDots(current: Int) {
        dots.clear()
        container_dots.removeAllViews()

        for (i in 0 until adapter.count) {
            dots.add(TextView(this))
            dots[i].text = resources.getString(R.string.dot)
            dots[i].textSize = 35f
            dots[i].setTextColor(colorsInactive[current])
            container_dots.addView(dots[i])
        }

        if(dots.size > 0) dots[current].setTextColor(colorsActive[current])
    }

    private fun nextPage(current: Int) {
        if(current < pages) viewpager.currentItem = current else startMainActivity()
    }

    private fun startMainActivity() {
        sp.edit().putBoolean(Constants.SP_ONBOARD, true).apply()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
