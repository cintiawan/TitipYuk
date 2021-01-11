package xyz.cintiawan.titipyuk.ui.titipan.list

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.customview.WrapContentLinearLayoutManager
import xyz.cintiawan.titipyuk.databinding.ActivityTitipanUserListBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.ui.titipan.item.TitipanViewAdapter
import javax.inject.Inject

class TitipanUserListActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: TitipanUserListViewModel

    private lateinit var binding: ActivityTitipanUserListBinding

    // Adapter
    lateinit var adapter: TitipanViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_titipan_user_list)
        binding.swipeRefresh.setColorSchemeResources(R.color.swipe_refresh)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Titipan Saya"

        initViewModel()
        initBinding()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(TitipanUserListViewModel::class.java)
        adapter = TitipanViewAdapter { viewModel.retry() }

        viewModel.titipans.observe(this, Observer { adapter.submitList(it) })
        viewModel.state.observe(this, Observer { adapter.setState(it) })
    }

    private fun initBinding() {
        binding.titipanList.adapter = adapter
        binding.titipanList.layoutManager = WrapContentLinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.viewModel = viewModel
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }

}
