package xyz.cintiawan.titipyuk.ui.preorder.list

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.customview.WrapContentLinearLayoutManager
import xyz.cintiawan.titipyuk.databinding.ActivityPreOrderVerticalListBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.model.filter.PreOrderFilter
import xyz.cintiawan.titipyuk.ui.preorder.item.vertical.PreOrderVerticalViewAdapter
import javax.inject.Inject

class PreOrderVerticalListActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: PreOrderListViewModel

    private lateinit var binding: ActivityPreOrderVerticalListBinding

    // Adapter
    private lateinit var adapter: PreOrderVerticalViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pre_order_vertical_list)
        binding.swipeRefresh.setColorSchemeResources(R.color.swipe_refresh)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Pre Order Saya"

        initViewModel()
        initBinding()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initViewModel() {
        factory.injectParam(PreOrderFilter(true))
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(PreOrderListViewModel::class.java)
        adapter = PreOrderVerticalViewAdapter { viewModel.retry() }

        viewModel.preorders.observe(this, Observer { adapter.submitList(it) })
        viewModel.state.observe(this, Observer { adapter.setState(it) })
    }

    private fun initBinding() {
        binding.preorderList.adapter = adapter
        binding.preorderList.layoutManager = WrapContentLinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.viewModel = viewModel
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }

}
