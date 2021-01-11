package xyz.cintiawan.titipyuk.ui.list

import android.os.Bundle
import android.view.Menu
import kotlinx.android.synthetic.main.activity_list.*
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.ui.preorder.list.PreOrderListFragment
import xyz.cintiawan.titipyuk.ui.request.list.RequestListFragment
import xyz.cintiawan.titipyuk.ui.trip.list.TripListFragment

class ListActivity : BaseActivity() {
    // Adapter
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(PreOrderListFragment())
        adapter.addFragment(RequestListFragment())
        adapter.addFragment(TripListFragment())

        viewpager.adapter = adapter
        viewpager.currentItem = intent.getIntExtra(TIPE_ID, 0)
        viewpager.offscreenPageLimit = 0

        tabs.setupWithViewPager(viewpager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.list_toolbar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val TIPE_ID = "tipe_id"
    }

}
