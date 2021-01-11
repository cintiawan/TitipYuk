package xyz.cintiawan.titipyuk.ui.request.story

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_story_request.*
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.model.item.RequestItem
import xyz.cintiawan.titipyuk.ui.story.StoryViewPagerAdapter

class StoryRequestActivity : BaseActivity() {
    // Adapter
    private lateinit var adapter: StoryViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_request)

        // Change status bar color
        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = Color.BLACK
        }

        adapter = StoryViewPagerAdapter(supportFragmentManager)
        intent.getParcelableArrayListExtra<RequestItem>(OBJECT_LIST).forEach { if(it != null) adapter.addFragment(StoryRequestFragment.newInstance(it)) }

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
