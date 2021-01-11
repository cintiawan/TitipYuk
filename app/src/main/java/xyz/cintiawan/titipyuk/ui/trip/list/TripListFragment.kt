package xyz.cintiawan.titipyuk.ui.trip.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseFragment
import xyz.cintiawan.titipyuk.customview.WrapContentLinearLayoutManager
import xyz.cintiawan.titipyuk.databinding.FragmentTripListBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.model.filter.TripFilter
import xyz.cintiawan.titipyuk.ui.trip.item.vertical.TripVerticalViewAdapter
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TripListFragment : BaseFragment() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: TripListViewModel

    private lateinit var binding: FragmentTripListBinding

    // Adapter
    private lateinit var tripAdapter: TripVerticalViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.inject()
        binding.swipeRefresh.setColorSchemeResources(R.color.swipe_refresh)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
        initBinding()
    }

    private fun initViewModel() {
        factory.injectParam(TripFilter(false))
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(TripListViewModel::class.java)

        tripAdapter = TripVerticalViewAdapter(resources.getString(R.string.trip_title)) { viewModel.retry() }

        viewModel.trips.observe(this, Observer { tripAdapter.submitList(it) })
        viewModel.state.observe(this, Observer { tripAdapter.setState(it) })
    }

    private fun initBinding() {
        binding.tripList.adapter = tripAdapter
        binding.tripList.layoutManager = WrapContentLinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.viewModel = viewModel
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }

    override fun toString(): String = "Trip"

}
