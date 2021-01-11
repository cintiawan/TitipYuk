package xyz.cintiawan.titipyuk.ui.request.detail.confirm

import android.os.Bundle
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
import xyz.cintiawan.titipyuk.customview.ErrorDialog
import xyz.cintiawan.titipyuk.customview.LoadingDialog
import xyz.cintiawan.titipyuk.customview.SuccessDialog
import xyz.cintiawan.titipyuk.databinding.BottomSheetFragmentRequestConfirmBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.model.item.CityItem
import xyz.cintiawan.titipyuk.model.item.CityOngkirItem
import xyz.cintiawan.titipyuk.ui.city.item.autocomplete.CityAutocompleteAdapter
import xyz.cintiawan.titipyuk.ui.city.item.autocomplete.CityOngkirAutoCompleteAdapter
import xyz.cintiawan.titipyuk.util.State
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
class RequestConfirmBottomSheetFragment : BaseBottomSheetDialogFragment() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: RequestConfirmViewModel

    private lateinit var binding: BottomSheetFragmentRequestConfirmBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var errorDialog: ErrorDialog
    private lateinit var successDialog: SuccessDialog

    // Adapter
    private lateinit var kotaAsalAdapter: CityAutocompleteAdapter
    private lateinit var kotaDikirimDariAdapter: CityOngkirAutoCompleteAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_fragment_request_confirm, container, false)
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
                .get(RequestConfirmViewModel::class.java)

        viewModel.kotaItem.observe(this, Observer {
            kotaAsalAdapter = CityAutocompleteAdapter(context!!, R.layout.item_city_autocomplete)
            binding.txtKotaAsal.setAdapter(kotaAsalAdapter)
            binding.txtKotaAsal.threshold = 0

            kotaAsalAdapter.setData(it)
        })

        viewModel.kotaOngkirItem.observe(this, Observer {
            kotaDikirimDariAdapter = CityOngkirAutoCompleteAdapter(context!!, R.layout.item_city_ongkir_autocomplete)
            binding.txtKotaDikirim.setAdapter(kotaDikirimDariAdapter)
            binding.txtKotaDikirim.threshold = 0

            kotaDikirimDariAdapter.setData(it)
        })
    }

    private fun initBinding() {
        binding.viewModel = viewModel

        binding.edtHarga.afterTextChanged { viewModel.validateHarga(it) }
        binding.edtBerat.afterTextChanged { viewModel.validateBerat(it) }
        binding.txtKotaAsal.setOnItemClickListener { adapterView, _, position, _ -> viewModel.selectKotaAsal(adapterView.adapter.getItem(position) as CityItem) }
        binding.txtKotaDikirim.setOnItemClickListener { adapterView, _, position, _ -> viewModel.selectDikirimDari(adapterView.adapter.getItem(position) as CityOngkirItem) }
        binding.edtTanggalPengiriman.afterTextChanged { viewModel.validateTanggalPengiriman(it) }
        binding.btnConfirm.setOnClickListener { arguments?.getInt(ID, 0)?.let { it1 -> viewModel.postConfirmation(it1) } }
    }

    private fun initDialog() {
        loadingDialog = LoadingDialog(context!!)
        errorDialog = ErrorDialog(context!!)
        successDialog = SuccessDialog(context!!)

        viewModel.stateInit.observe(this, Observer { loadingDialog.loading(it == State.LOADING) })
        viewModel.errorMessageInit.observe(this, Observer { error(it) })

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

        fun newInstance(id: Int): RequestConfirmBottomSheetFragment {
            val fragment = RequestConfirmBottomSheetFragment()
            val args = Bundle()
            args.putInt(ID, id)
            fragment.arguments = args

            return fragment
        }
    }

}
