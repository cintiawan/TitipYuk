package xyz.cintiawan.titipyuk.ui.story

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class StoryViewPagerAdapter(fragmentManager: FragmentManager?) : FragmentPagerAdapter(fragmentManager) {
    private var fragmentList = mutableListOf<Fragment>()

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? = fragmentList[position].toString()

    fun addFragment(f: Fragment) = fragmentList.add(f)

    fun addAllFragments(fs: List<Fragment>) {
        fragmentList.addAll(fs)
    }
}