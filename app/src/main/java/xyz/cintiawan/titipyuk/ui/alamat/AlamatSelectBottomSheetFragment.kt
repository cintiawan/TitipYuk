package xyz.cintiawan.titipyuk.ui.alamat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseBottomSheetDialogFragment
import xyz.cintiawan.titipyuk.customview.WrapContentLinearLayoutManager
import xyz.cintiawan.titipyuk.databinding.BottomSheetFragmentAlamatSelectBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.model.item.AlamatItem
import xyz.cintiawan.titipyuk.ui.alamat.item.list.AlamatCallback
import xyz.cintiawan.titipyuk.ui.alamat.item.selectable.AlamatSelectViewAdapter
import xyz.cintiawan.titipyuk.ui.alamat.post.PostAlamatActivity
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
@SuppressLint("ValidFragment")
class AlamatSelectBottomSheetFragment(private val callback: AlamatCallback) : BaseBottomSheetDialogFragment() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: AlamatViewModel

    private lateinit var binding: BottomSheetFragmentAlamatSelectBinding

    // Adapter
    lateinit var adapter: AlamatSelectViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_fragment_alamat_select, container, false)
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

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(AlamatViewModel::class.java)
        adapter = AlamatSelectViewAdapter({ viewModel.retry() }, { itemClick(it) })

        viewModel.data.observe(this, Observer { adapter.updateList(it) })
        viewModel.state.observe(this, Observer { adapter.setState(it) })
    }

    private fun initBinding() {
        binding.alamatList.adapter = adapter
        binding.alamatList.layoutManager = WrapContentLinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.viewModel = viewModel
        binding.btnTambahAlamat.setOnClickListener { startActivity(Intent(context, PostAlamatActivity::class.java)) }
    }

    private fun itemClick(data: AlamatItem) {
        callback.setAlamat(data)
        dismiss()
    }

}
