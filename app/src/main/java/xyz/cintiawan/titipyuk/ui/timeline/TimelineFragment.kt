package xyz.cintiawan.titipyuk.ui.timeline


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseFragment
import xyz.cintiawan.titipyuk.databinding.FragmentTimelineBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.ui.timeline.post.PostViewAdapter
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TimelineFragment : BaseFragment() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: TimelineViewModel

    private lateinit var binding: FragmentTimelineBinding

    // Adapter
    lateinit var postAdapter: PostViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.inject()
        binding.swipeRefresh.setColorSchemeResources(R.color.swipe_refresh)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()
        initViewModel()
        initBinding()
    }

    private fun initAdapter() {
        postAdapter = PostViewAdapter()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(TimelineViewModel::class.java)

        viewModel.posts.observe(this, Observer { postAdapter.updateList(it) })
    }

    private fun initBinding() {
        binding.postList.adapter = postAdapter
        binding.postList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.viewModel = viewModel
        binding.swipeRefresh.setOnRefreshListener {  }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.main_toolbar_menu_1, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

}
