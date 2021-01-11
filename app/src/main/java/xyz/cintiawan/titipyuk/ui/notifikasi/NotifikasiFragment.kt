package xyz.cintiawan.titipyuk.ui.notifikasi


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseFragment
import xyz.cintiawan.titipyuk.databinding.FragmentNotifikasiBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class NotifikasiFragment : BaseFragment() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: NotifikasiViewModel

    private lateinit var binding: FragmentNotifikasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifikasi, container, false)
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
        inflater?.inflate(R.menu.main_toolbar_menu_1, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(NotifikasiViewModel::class.java)
    }

    private fun initBinding() {
        binding.viewModel = viewModel
    }

}
