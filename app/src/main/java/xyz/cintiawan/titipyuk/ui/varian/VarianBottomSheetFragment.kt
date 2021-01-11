package xyz.cintiawan.titipyuk.ui.varian

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseBottomSheetDialogFragment
import xyz.cintiawan.titipyuk.databinding.BottomSheetFragmentVarianBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.util.afterTextChanged
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
class VarianBottomSheetFragment(private val callback: VarianCallback) : BaseBottomSheetDialogFragment() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: VarianViewModel

    private lateinit var binding: BottomSheetFragmentVarianBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_fragment_varian, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
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

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(VarianViewModel::class.java)
    }

    private fun initBinding() {
        binding.viewModel = viewModel

        binding.edtNama.afterTextChanged { viewModel.validateNama(it) }
        binding.edtHarga.afterTextChanged { viewModel.validateHarga(it) }
        binding.btnConfirm.setOnClickListener {
            callback.setVarian(viewModel.nama, viewModel.harga)
            dismiss()
        }
    }

}
