package xyz.cintiawan.titipyuk.ui.preorder.story

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_story_pre_order.*
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.model.item.PreOrderItem
import xyz.cintiawan.titipyuk.ui.story.StoryViewPagerAdapter

class StoryPreOrderActivity : BaseActivity() {
    // Adapter
    private lateinit var adapter: StoryViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_pre_order)

        // Change status bar color
        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = Color.BLACK
        }

        adapter = StoryViewPagerAdapter(supportFragmentManager)
        intent.getParcelableArrayListExtra<PreOrderItem>(OBJECT_LIST).forEach { if(it != null) adapter.addFragment(StoryPreOrderFragment.newInstance(it)) }

        viewpager.adapter = adapter
        viewpager.currentItem = intent.getIntExtra(OBJECT_ID, 0)
    }

    fun nextFragment() {
        viewpager.currentItem++
        if(viewpager.currentItem - 1 >= adapter.count) finish()
    }

    fun prevFragment() {
        viewpager.currentItem--
    }

    companion object {
        const val OBJECT_ID = "object_id"
        const val OBJECT_LIST = "object_list"
    }

}
