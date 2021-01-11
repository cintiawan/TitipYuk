package xyz.cintiawan.titipyuk.ui.slider

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SliderBarangViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private var fragmentList = mutableListOf<Fragment>()

    override fun getItem(position: Int): Fragment =
            SliderBarangFragment.newInstance(fragmentList[position % fragmentList.size].arguments?.getString(SliderBarangFragment.IMAGE))

    override fun getCount(): Int = fragmentList.size

    fun addAllFragments(urls: List<String>) {
        for (i in 0 until urls.size) fragmentList.add(SliderBarangFragment.newInstance(urls[i]))
    }
}