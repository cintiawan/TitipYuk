package xyz.cintiawan.titipyuk.ui.intro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter


class IntroViewPagerAdapter : PagerAdapter() {
    private lateinit var layoutInflater: LayoutInflater
    private var layouts: MutableList<Int> = mutableListOf()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(container.context)

        val view = layoutInflater.inflate(layouts[position], container, false)
        container.addView(view)

        return view
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

    override fun getCount(): Int = layouts.size

    fun addLayout(layout: Int) = layouts.add(layout)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}