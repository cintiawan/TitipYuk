package xyz.cintiawan.titipyuk.ui.slider

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SliderViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private var fragmentList = mutableListOf<Fragment>()

    override fun getItem(position: Int): Fragment =
            SliderFragment.newInstance(fragmentList[position % fragmentList.size].arguments?.getString(SliderFragment.IMAGE))

    override fun getCount(): Int = Int.MAX_VALUE

    fun addAllFragments(urls: List<String>) {
        for (i in 0 until urls.size) fragmentList.add(SliderFragment.newInstance(urls[i]))
    }
}