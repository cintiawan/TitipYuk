package xyz.cintiawan.titipyuk.ui.beranda


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseFragment
import xyz.cintiawan.titipyuk.customview.ResizedLinearLayoutManager
import xyz.cintiawan.titipyuk.customview.SliderIndicator
import xyz.cintiawan.titipyuk.databinding.FragmentBerandaBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.ui.chat.LatestChatActivity
import xyz.cintiawan.titipyuk.ui.city.item.horizontal.CityHorizontalViewAdapter
import xyz.cintiawan.titipyuk.ui.list.ListActivity
import xyz.cintiawan.titipyuk.ui.negara.item.NegaraHorizontalViewAdapter
import xyz.cintiawan.titipyuk.ui.preorder.item.horizontal.PreOrderHorizontalViewAdapter
import xyz.cintiawan.titipyuk.ui.request.item.horizontal.RequestHorizontalViewAdapter
import xyz.cintiawan.titipyuk.ui.slider.SliderViewPagerAdapter
import xyz.cintiawan.titipyuk.ui.trip.item.horizontal.TripHorizontalViewAdapter
import xyz.cintiawan.titipyuk.ui.user.auth.AuthActivity
import xyz.cintiawan.titipyuk.util.Constants
import xyz.cintiawan.titipyuk.util.SPUtil
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the beranda initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class BerandaFragment : BaseFragment() {
    @Inject lateinit var factory: ViewModelFactory
    @Inject lateinit var toast: Toast
    @Inject lateinit var sp: SharedPreferences
    private lateinit var viewModel: BerandaViewModel

    private lateinit var binding: FragmentBerandaBinding

    // Adapter
    private lateinit var sliderAdapter: SliderViewPagerAdapter
    private lateinit var preOrderAdapter: PreOrderHorizontalViewAdapter
    private lateinit var requestAdapter: RequestHorizontalViewAdapter
    private lateinit var tripAdapter: TripHorizontalViewAdapter
    private lateinit var negaraAdapter: NegaraHorizontalViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_beranda, container, false)
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.main_toolbar_menu_1, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (!SPUtil.hasAccessToken(sp)) {
            toast.setText("Silahkan login terlebih dahulu")
            toast.duration = Toast.LENGTH_SHORT
            toast.show()

            startActivity(Intent(context, AuthActivity::class.java))
        } else {
            when(item?.itemId) {
                R.id.box_titipan -> { }
                R.id.history_chat -> startActivity(Intent(context, LatestChatActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        viewModel.preorders.value?.dataSource?.invalidate()
        viewModel.requests.value?.dataSource?.invalidate()
        viewModel.trips.value?.dataSource?.invalidate()
        viewModel.negaras.value?.dataSource?.invalidate()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(BerandaViewModel::class.java)

        preOrderAdapter = PreOrderHorizontalViewAdapter { viewModel.preOrderRetry() }
        requestAdapter = RequestHorizontalViewAdapter { viewModel.requestRetry() }
        tripAdapter = TripHorizontalViewAdapter { viewModel.tripRetry() }
        negaraAdapter = NegaraHorizontalViewAdapter { viewModel.cityRetry() }

        viewModel.sliderImages.observe(this, Observer {
            if(it != null && it.isNotEmpty()) {
                sliderAdapter = SliderViewPagerAdapter(childFragmentManager)
                sliderAdapter.addAllFragments(it)

                val indicator = SliderIndicator(context!!, binding.imagesContainer, binding.slider, R.drawable.slider_indicator_circle)
                indicator.setPageCount(it.size)
                indicator.show()

                binding.slider.adapter = sliderAdapter
            }
        })

        viewModel.preorders.observe(this, Observer { preOrderAdapter.submitList(it) })
        viewModel.requests.observe(this, Observer { requestAdapter.submitList(it) })
        viewModel.trips.observe(this, Observer { tripAdapter.submitList(it) })
        viewModel.negaras.observe(this, Observer { negaraAdapter.submitList(it) })

        viewModel.preOrderState.observe(this, Observer { preOrderAdapter.setState(it) })
        viewModel.requestState.observe(this, Observer { requestAdapter.setState(it) })
        viewModel.tripState.observe(this, Observer { tripAdapter.setState(it) })
        viewModel.negaraState.observe(this, Observer { negaraAdapter.setState(it) })
    }

    private fun initBinding() {
        binding.preorderList.adapter = preOrderAdapter
        binding.preorderList.layoutManager = ResizedLinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        binding.requestList.adapter = requestAdapter
        binding.requestList.layoutManager = ResizedLinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        binding.tripList.adapter = tripAdapter
        binding.tripList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        binding.cityList.adapter = negaraAdapter
        binding.cityList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        binding.viewModel = viewModel
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
        binding.btnRetry.setOnClickListener { viewModel.loadSliderImages() }
        binding.btnListPreorder.setOnClickListener {
            startActivity(Intent(context, ListActivity::class.java)
                    .putExtra(ListActivity.TIPE_ID, Constants.STATE_PREORDER))
        }
        binding.btnListRequest.setOnClickListener {
            startActivity(Intent(context, ListActivity::class.java)
                    .putExtra(ListActivity.TIPE_ID, Constants.STATE_REQUEST))
        }
        binding.btnListTrip.setOnClickListener {
            startActivity(Intent(context, ListActivity::class.java)
                    .putExtra(ListActivity.TIPE_ID, Constants.STATE_TRIP))
        }
    }

}
