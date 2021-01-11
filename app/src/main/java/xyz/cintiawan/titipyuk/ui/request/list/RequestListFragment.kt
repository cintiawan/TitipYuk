package xyz.cintiawan.titipyuk.ui.request.list

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseFragment
import xyz.cintiawan.titipyuk.customview.AutoFitGridLayoutManager
import xyz.cintiawan.titipyuk.databinding.FragmentRequestListBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.model.filter.RequestFilter
import xyz.cintiawan.titipyuk.ui.request.item.grid.RequestGridViewAdapter
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RequestListFragment.OnFragmentInteractionListener] interface
 * kotaTujuan handle interaction events.
 *
 */
class RequestListFragment : BaseFragment() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: RequestListViewModel

    private lateinit var binding: FragmentRequestListBinding

    // Adapter
    lateinit var requestAdapter: RequestGridViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_list, container, false)
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
        factory.injectParam(RequestFilter(false))
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(RequestListViewModel::class.java)

        requestAdapter = RequestGridViewAdapter(resources.getString(R.string.request_title)) { viewModel.retry() }

        viewModel.requests.observe(this, Observer { requestAdapter.submitList(it) })
        viewModel.state.observe(this, Observer { requestAdapter.setState(it) })
    }

    private fun initBinding() {
        binding.requestList.adapter = requestAdapter

        val cols = 3
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val columns = displayMetrics.widthPixels / cols
        val layoutManager = AutoFitGridLayoutManager(context, columns)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when {
                    position == 0 -> cols
                    position == (requestAdapter.itemCount - 1) && requestAdapter.hasFooter() -> cols
                    else -> 1
                }
            }
        }
        binding.requestList.layoutManager = layoutManager

        binding.viewModel = viewModel
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }

    override fun toString(): String = "Request"

}
