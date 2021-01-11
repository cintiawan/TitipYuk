package xyz.cintiawan.titipyuk.ui.user.profile


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseFragment
import xyz.cintiawan.titipyuk.databinding.FragmentProfileBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.ui.list.ViewPagerAdapter
import xyz.cintiawan.titipyuk.ui.user.profile.setting.UserSettingActivity
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class UserFragment : BaseFragment() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: ProfileViewModel

    private lateinit var binding: FragmentProfileBinding

    // Adapter
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.inject()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
        initBinding()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.main_toolbar_menu_2, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.user_setting -> {
                startActivity(Intent(activity, UserSettingActivity::class.java))
                true
            }
            else -> false
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(ProfileViewModel::class.java)

        adapter = ViewPagerAdapter(fragmentManager)
        adapter.addFragment(ProfilePenitipFragment())
        adapter.addFragment(ProfileShopperFragment())
    }

    private fun initBinding() {
        binding.viewpager.adapter = adapter

        binding.viewModel = viewModel
        binding.btnRetry.setOnClickListener { viewModel.retry() }
        binding.tabs.setupWithViewPager(binding.viewpager)
    }

}
