package xyz.cintiawan.titipyuk.ui.trip.list

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.customview.WrapContentLinearLayoutManager
import xyz.cintiawan.titipyuk.databinding.ActivityTripVerticalListBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.model.filter.TripFilter
import xyz.cintiawan.titipyuk.ui.trip.item.vertical.TripUserVerticalViewAdapter
import xyz.cintiawan.titipyuk.ui.trip.item.vertical.TripVerticalViewAdapter
import javax.inject.Inject

class TripVerticalListActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: TripListViewModel

    private lateinit var binding: ActivityTripVerticalListBinding

    // Adapter
    private lateinit var adapter: TripUserVerticalViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_vertical_list)
        binding.swipeRefresh.setColorSchemeResources(R.color.swipe_refresh)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Trip Saya"

        initViewModel()
        initBinding()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initViewModel() {
        factory.injectParam(TripFilter(true))
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(TripListViewModel::class.java)
        adapter = TripUserVerticalViewAdapter { viewModel.retry() }

        viewModel.trips.observe(this, Observer { adapter.submitList(it) })
        viewModel.state.observe(this, Observer { adapter.setState(it) })
    }

    private fun initBinding() {
        binding.preorderList.adapter = adapter
        binding.preorderList.layoutManager = WrapContentLinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.viewModel = viewModel
    }

}
