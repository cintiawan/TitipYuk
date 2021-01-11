package xyz.cintiawan.titipyuk.ui.preorder.detail

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.customview.SliderIndicator
import xyz.cintiawan.titipyuk.databinding.ActivityPreOrderDetailBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.ui.chat.ChatChannelActivity
import xyz.cintiawan.titipyuk.ui.preorder.detail.confirm.PreOrderConfirmBottomSheetFragment
import xyz.cintiawan.titipyuk.ui.review.item.list.ReviewViewAdapter
import xyz.cintiawan.titipyuk.ui.review.list.ReviewUserListActivity
import xyz.cintiawan.titipyuk.ui.slider.SliderBarangViewPagerAdapter
import javax.inject.Inject

class PreOrderDetailActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: PreOrderDetailViewModel

    private lateinit var binding: ActivityPreOrderDetailBinding
    private lateinit var confirmationForm: PreOrderConfirmBottomSheetFragment
    private var id = 0

    // Adapter
    private lateinit var sliderAdapter: SliderBarangViewPagerAdapter
    private lateinit var reviewAdapter: ReviewViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pre_order_detail)

        // Change status bar color
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        id = intent.getIntExtra(PREORDER_ID, 0)

        initViewModel()
        initBinding()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.detail_toolbar_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initViewModel() {
        factory.injectParam(id)
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(PreOrderDetailViewModel::class.java)
        reviewAdapter = ReviewViewAdapter()

        viewModel.barangName.observe(this, Observer {
            if(it != null) {
                binding.collapsingToolbar.title = it
                binding.collapsingToolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                binding.collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent))
            }
        })
        viewModel.barangImages.observe(this, Observer {
            if(it != null) {
                sliderAdapter = SliderBarangViewPagerAdapter(supportFragmentManager)
                sliderAdapter.addAllFragments(it)

                val indicator = SliderIndicator(this, binding.imagesContainer, binding.slider, R.drawable.slider_indicator_circle)
                indicator.setPageCount(it.size)
                indicator.setPageAutoScroll(false)
                indicator.show()

                binding.slider.adapter = sliderAdapter
            }
        })
        viewModel.reviews.observe(this, Observer { reviewAdapter.updateList(it.take(3)) })
        viewModel.varians.observe(this, Observer { confirmationForm = PreOrderConfirmBottomSheetFragment.newInstance(id, it) })
    }

    private fun initBinding() {
        binding.ratingList.adapter = reviewAdapter
        binding.ratingList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.viewModel = viewModel
        binding.btnRetry.setOnClickListener { viewModel.retry() }
        binding.btnReviews.setOnClickListener {
            startActivity(Intent(this, ReviewUserListActivity::class.java)
                    .putExtra(ReviewUserListActivity.AUTH, false)
                    .putExtra(ReviewUserListActivity.USER_ID, viewModel.userIdParam)
                    .putExtra(ReviewUserListActivity.USER_NAME, viewModel.userNameParam))
        }
        binding.btnChat.setOnClickListener {
            startActivity(Intent(this, ChatChannelActivity::class.java)
                    .putExtra(ChatChannelActivity.OTHER_USER_ID, viewModel.userUid))
        }
        binding.btnConfirm.setOnClickListener { loadConfirmationSheet() }
    }

    private fun loadConfirmationSheet() {
        confirmationForm.show(supportFragmentManager, PreOrderConfirmBottomSheetFragment::class.simpleName)
    }

    companion object {
        const val PREORDER_ID = "preorder_id"
    }

}
