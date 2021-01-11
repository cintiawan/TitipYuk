package xyz.cintiawan.titipyuk.ui.review.list

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.customview.WrapContentLinearLayoutManager
import xyz.cintiawan.titipyuk.databinding.ActivityReviewUserListBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.model.filter.ReviewFilter
import xyz.cintiawan.titipyuk.ui.review.item.paged.ReviewViewPagedAdapter
import javax.inject.Inject

class ReviewUserListActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: ReviewListViewModel

    private lateinit var binding: ActivityReviewUserListBinding

    // Adapter
    lateinit var adapter: ReviewViewPagedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_user_list)
        binding.swipeRefresh.setColorSchemeResources(R.color.swipe_refresh)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Penilaian Terhadap " +
                if(intent.getStringExtra(USER_NAME).isNullOrEmpty()) "Saya" else intent.getStringExtra(USER_NAME)

        initViewModel()
        initBinding()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initViewModel() {
        factory.injectParam(ReviewFilter(intent.getBooleanExtra(AUTH, true), intent.getIntExtra(USER_ID, 0)))
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(ReviewListViewModel::class.java)
        adapter = ReviewViewPagedAdapter { viewModel.retry() }

        viewModel.reviews.observe(this, Observer { adapter.submitList(it) })
        viewModel.state.observe(this, Observer { adapter.setState(it) })
    }

    private fun initBinding() {
        binding.ratingList.adapter = adapter
        binding.ratingList.layoutManager = WrapContentLinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.viewModel = viewModel
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }

    companion object {
        const val AUTH = "auth"
        const val USER_ID = "user_id"
        const val USER_NAME = "user_name"
    }

}
