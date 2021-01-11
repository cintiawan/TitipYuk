package xyz.cintiawan.titipyuk.ui.request.list

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.customview.WrapContentLinearLayoutManager
import xyz.cintiawan.titipyuk.databinding.ActivityRequestVerticalListBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.model.filter.RequestFilter
import xyz.cintiawan.titipyuk.ui.request.item.vertical.RequestVerticalViewAdapter
import javax.inject.Inject

class RequestVerticalListActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: RequestListViewModel

    private lateinit var binding: ActivityRequestVerticalListBinding

    // Adapter
    private lateinit var adapter: RequestVerticalViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_vertical_list)
        binding.swipeRefresh.setColorSchemeResources(R.color.swipe_refresh)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Request Saya"

        initViewModel()
        initBinding()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initViewModel() {
        factory.injectParam(RequestFilter(true))
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(RequestListViewModel::class.java)
        adapter = RequestVerticalViewAdapter { viewModel.retry() }

        viewModel.requests.observe(this, Observer { adapter.submitList(it) })
        viewModel.state.observe(this, Observer { adapter.setState(it) })
    }

    private fun initBinding() {
        binding.preorderList.adapter = adapter
        binding.preorderList.layoutManager = WrapContentLinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.viewModel = viewModel
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }

}
