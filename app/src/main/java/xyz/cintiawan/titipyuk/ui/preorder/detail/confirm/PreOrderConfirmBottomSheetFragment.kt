package xyz.cintiawan.titipyuk.ui.preorder.detail.confirm

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseBottomSheetDialogFragment
import xyz.cintiawan.titipyuk.customview.ConfirmationDialog
import xyz.cintiawan.titipyuk.customview.ErrorDialog
import xyz.cintiawan.titipyuk.customview.LoadingDialog
import xyz.cintiawan.titipyuk.customview.SuccessDialog
import xyz.cintiawan.titipyuk.databinding.BottomSheetFragmentPreOrderConfirmBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.model.item.VarianItem
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.afterTextChanged
import java.util.ArrayList
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PreOrderConfirmBottomSheetFragment : BaseBottomSheetDialogFragment() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: PreOrderConfirmViewModel

    private lateinit var binding: BottomSheetFragmentPreOrderConfirmBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var errorDialog: ErrorDialog
    private lateinit var successDialog: SuccessDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_fragment_pre_order_confirm, container, false)
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
        initDialog()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(PreOrderConfirmViewModel::class.java)
    }

    private fun initBinding() {
        binding.viewModel = viewModel
        arguments?.getParcelableArrayList<VarianItem>(VARIAN)?.let { varians ->
            binding.spinnerVarian.setItems(varians)
            binding.spinnerVarian.setOnItemSelectedListener { _, _, _, item -> viewModel.selectVarian(item as VarianItem) }
            binding.edtJumlah.afterTextChanged { viewModel.validateJumlah(it) }
            binding.edtCatatan.afterTextChanged { viewModel.validateCatatan(it) }
            binding.btnConfirm.setOnClickListener { arguments?.getInt(ID, 0)?.let { it1 -> viewModel.postConfirmation(it1) } }
        }
    }

    private fun initDialog() {
        loadingDialog = LoadingDialog(context!!)
        errorDialog = ErrorDialog(context!!)
        successDialog = SuccessDialog(context!!)

        viewModel.statePost.observe(this, Observer { loadingDialog.loading(it == State.LOADING) })
        viewModel.errorMessagePost.observe(this, Observer { error(it) })
        viewModel.successMessage.observe(this, Observer { success(it) })
    }

    private fun error(msg: String) {
        errorDialog.setMessage(msg)
        errorDialog.setRetry {
            viewModel.retry()
            errorDialog.dismiss()
        }
        if(msg.isNotEmpty()) errorDialog.show()
    }

    private fun success(msg: String) {
        successDialog.setMessage(msg)
        successDialog.setSuccess { dismiss() }
        successDialog.show()
    }

    companion object {
        const val ID = "id"
        const val VARIAN = "varian"

        fun newInstance(id: Int, data: List<VarianItem>?): PreOrderConfirmBottomSheetFragment {
            val fragment = PreOrderConfirmBottomSheetFragment()
            val args = Bundle()
            args.putInt(ID, id)
            args.putParcelableArrayList(VARIAN, data as ArrayList<out Parcelable>)
            fragment.arguments = args

            return fragment
        }
    }

}
