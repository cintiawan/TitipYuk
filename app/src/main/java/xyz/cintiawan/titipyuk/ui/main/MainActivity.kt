package xyz.cintiawan.titipyuk.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import com.wangjie.rapidfloatingactionbutton.util.RFABShape
import com.wangjie.rapidfloatingactionbutton.util.RFABTextUtil
import kotlinx.android.synthetic.main.activity_main.*
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.ui.beranda.BerandaFragment
import xyz.cintiawan.titipyuk.ui.notifikasi.NotifikasiFragment
import xyz.cintiawan.titipyuk.ui.preorder.post.PostPreOrderActivity
import xyz.cintiawan.titipyuk.ui.request.post.PostRequestActivity
import xyz.cintiawan.titipyuk.ui.timeline.TimelineFragment
import xyz.cintiawan.titipyuk.ui.trip.post.PostTripActivity
import xyz.cintiawan.titipyuk.ui.user.auth.AuthActivity
import xyz.cintiawan.titipyuk.ui.user.auth.RedirectFragment
import xyz.cintiawan.titipyuk.ui.user.profile.UserFragment
import xyz.cintiawan.titipyuk.util.SPUtil
import xyz.cintiawan.titipyuk.util.gone
import xyz.cintiawan.titipyuk.util.visible
import javax.inject.Inject

class MainActivity : BaseActivity(), RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener<Any> {
    @Inject lateinit var toast: Toast
    @Inject lateinit var sp: SharedPreferences

    private val beranda: Fragment = BerandaFragment()
    private var timeline: Fragment = Fragment()
    private var notifikasi: Fragment = Fragment()
    private var user: Fragment = Fragment()
    private var active: Fragment = beranda

    private lateinit var rfaContent: RapidFloatingActionContentLabelList
    private lateinit var items: MutableList<RFACLabelItem<Any>>
    private lateinit var rfabHelper: RapidFloatingActionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        // Initiate beranda
        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, beranda, beranda.javaClass.simpleName)
                .commit()

        bottom_nav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.beranda -> loadBerandaFragment(savedInstanceState)
                R.id.timeline -> loadTimelineFragment(savedInstanceState)
                R.id.notifikasi -> loadNotifikasiFragment(savedInstanceState)
                R.id.saya -> loadUserFragment(savedInstanceState)
            }
            true
        }
        bottom_nav.selectedItemId = R.id.beranda

        // FAB
        rfaContent = RapidFloatingActionContentLabelList(this)
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this)

        items = mutableListOf()
        items.add(RFACLabelItem<Any>()
                .setLabel("Pre Order")
                .setLabelBackgroundDrawable(RFABShape.generateCornerShapeDrawable(ContextCompat.getColor(this, R.color.colorLightGrey), RFABTextUtil.dip2px(this, 4f)))
                .setResId(R.drawable.ic_shopping_bag)
                .setIconNormalColor(ContextCompat.getColor(this, R.color.colorLightRed))
                .setIconPressedColor(ContextCompat.getColor(this, R.color.colorLightGrey))
        )
        items.add(RFACLabelItem<Any>()
                .setLabel("Request")
                .setLabelBackgroundDrawable(RFABShape.generateCornerShapeDrawable(ContextCompat.getColor(this, R.color.colorLightGrey), RFABTextUtil.dip2px(this, 4f)))
                .setResId(R.drawable.ic_handshake)
                .setIconNormalColor(ContextCompat.getColor(this, R.color.colorGreen))
                .setIconPressedColor(ContextCompat.getColor(this, R.color.colorLightGrey))
        )
        items.add(RFACLabelItem<Any>()
                .setLabel("Trip")
                .setLabelBackgroundDrawable(RFABShape.generateCornerShapeDrawable(ContextCompat.getColor(this, R.color.colorLightGrey), RFABTextUtil.dip2px(this, 4f)))
                .setResId(R.drawable.ic_airplane_2)
                .setIconNormalColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .setIconPressedColor(ContextCompat.getColor(this, R.color.colorLightGrey))
        )

        rfaContent.setItems(items)
                .setIconShadowRadius(RFABTextUtil.dip2px(this, 5f))
                .setIconShadowColor(ContextCompat.getColor(this, R.color.colorGrey))

        rfabHelper = RapidFloatingActionHelper(this, fab_layout, fab_button, rfaContent).build()
    }

    override fun onRFACItemIconClick(position: Int, item: RFACLabelItem<Any>?) {
        loadPost(position)
        rfabHelper.toggleContent()
    }

    override fun onRFACItemLabelClick(position: Int, item: RFACLabelItem<Any>?) {
        return
    }

    private fun loadPost(position: Int) {
        if (!SPUtil.hasAccessToken(sp)) {
            toast.setText("Silahkan login terlebih dahulu")
            toast.duration = Toast.LENGTH_SHORT
            toast.show()

            startActivity(Intent(this, AuthActivity::class.java))
        } else {
            when (position) {
                0 -> startActivity(Intent(this, PostPreOrderActivity::class.java))
                1 -> startActivity(Intent(this, PostRequestActivity::class.java))
                2 -> startActivity(Intent(this, PostTripActivity::class.java))
            }
        }
    }

    private fun loadBerandaFragment(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            initBerandaFragment()
            supportFragmentManager
                    .beginTransaction()
                    .hide(active)
                    .show(beranda)
                    .commit()
            active = beranda
        }
    }

    private fun initBerandaFragment() {
        search_bar.visible()
        supportActionBar?.title = ""
    }

    private fun loadTimelineFragment(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            initTimelineFragment()
            supportFragmentManager
                    .beginTransaction()
                    .hide(active)
                    .show(timeline)
                    .commit()
            active = timeline
        }
    }

    private fun initTimelineFragment() {
        // UI
        search_bar.visible()
        supportActionBar?.title = ""

        if(timeline !is TimelineFragment) {
            timeline = TimelineFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, timeline, timeline.javaClass.simpleName)
                    .hide(timeline)
                    .commit()
        }
    }

    private fun loadNotifikasiFragment(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            initNotifikasiFragment()
            supportFragmentManager
                    .beginTransaction()
                    .hide(active)
                    .show(notifikasi)
                    .commit()
            active = notifikasi
        }
    }

    private fun initNotifikasiFragment() {
        // UI
        search_bar.gone()
        supportActionBar?.title = "Notifikasi"

        if(notifikasi !is RedirectFragment && notifikasi !is NotifikasiFragment) {
            if (!SPUtil.hasAccessToken(sp)) {
                if (notifikasi !is RedirectFragment) notifikasi = RedirectFragment()
            } else {
                if (notifikasi !is NotifikasiFragment) notifikasi = NotifikasiFragment()
            }
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, notifikasi, notifikasi.javaClass.simpleName)
                    .hide(notifikasi)
                    .commit()
        }
    }

    private fun loadUserFragment(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            initUserFragment()
            supportFragmentManager
                    .beginTransaction()
                    .hide(active)
                    .show(user)
                    .commit()
            active = user
        }
    }

    private fun initUserFragment() {
        // UI
        search_bar.gone()
        supportActionBar?.title = "Profil Saya"

        if(user !is RedirectFragment && user !is UserFragment) {
            if (!SPUtil.hasAccessToken(sp)) {
                if (user !is RedirectFragment) user = RedirectFragment()
            } else {
                if (user !is UserFragment) user = UserFragment()
            }
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, user, user.javaClass.simpleName)
                    .hide(user)
                    .commit()
        }
    }

    override fun onBackPressed() {
        if(!toast.view.isShown) {
            toast.setText("Tekan sekali lagi untuk keluar")
            toast.duration = Toast.LENGTH_SHORT
            toast.show()
        } else {
            toast.cancel()
            super.onBackPressed()
        }
    }

}
