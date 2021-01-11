package xyz.cintiawan.titipyuk.ui.trip.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.databinding.ActivityTripDetailBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.ui.chat.ChatChannelActivity
import xyz.cintiawan.titipyuk.ui.review.item.list.ReviewViewAdapter
import xyz.cintiawan.titipyuk.ui.review.list.ReviewUserListActivity
import xyz.cintiawan.titipyuk.ui.trip.detail.confirm.TripConfirmActivity
import javax.inject.Inject

class TripDetailActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: TripDetailViewModel

    private lateinit var binding: ActivityTripDetailBinding
    private var id = 0

    // Adapter
    private lateinit var reviewAdapter: ReviewViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_detail)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        id = intent.getIntExtra(TRIP_ID, 0)

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
                .get(TripDetailViewModel::class.java)
        reviewAdapter = ReviewViewAdapter()

        viewModel.reviews.observe(this, Observer { reviewAdapter.updateList(it.take(3)) })
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
        binding.btnConfirm.setOnClickListener {
            startActivity(Intent(this, TripConfirmActivity::class.java)
                    .putExtra(TripConfirmActivity.ID, id))
        }
    }

    companion object {
        const val TRIP_ID = "trip_id"
    }

}
