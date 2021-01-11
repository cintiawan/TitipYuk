package xyz.cintiawan.titipyuk.ui.trip.post

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.customview.ConfirmationDialog
import xyz.cintiawan.titipyuk.customview.ErrorDialog
import xyz.cintiawan.titipyuk.customview.LoadingDialog
import xyz.cintiawan.titipyuk.customview.SuccessDialog
import xyz.cintiawan.titipyuk.databinding.ActivityPostTripBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.model.item.CityItem
import xyz.cintiawan.titipyuk.model.item.CityOngkirItem
import xyz.cintiawan.titipyuk.ui.city.item.autocomplete.CityAutocompleteAdapter
import xyz.cintiawan.titipyuk.ui.city.item.autocomplete.CityOngkirAutoCompleteAdapter
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.afterTextChanged
import javax.inject.Inject

class PostTripActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: PostTripViewModel

    private lateinit var binding: ActivityPostTripBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var errorDialog: ErrorDialog
    private lateinit var confirmationDialog: ConfirmationDialog
    private lateinit var successDialog: SuccessDialog

    // Adapter
    private lateinit var kotaAsalAdapter: CityAutocompleteAdapter
    private lateinit var kotaTujuanAdapter: CityAutocompleteAdapter
    private lateinit var kotaDikirimDariAdapter: CityOngkirAutoCompleteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_trip)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Post Trip"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        initViewModel()
        initBinding()
        initDialog()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(PostTripViewModel::class.java)

        viewModel.kotaItem.observe(this, Observer {
            kotaAsalAdapter = CityAutocompleteAdapter(this, R.layout.item_city_autocomplete)
            kotaTujuanAdapter = CityAutocompleteAdapter(this, R.layout.item_city_autocomplete)
            binding.txtKotaAsal.setAdapter(kotaAsalAdapter)
            binding.txtKotaTujuan.setAdapter(kotaTujuanAdapter)
            binding.txtKotaAsal.threshold = 1
            binding.txtKotaTujuan.threshold = 1

            kotaAsalAdapter.setData(it)
            kotaTujuanAdapter.setData(it)
        })

        viewModel.kotaOngkirItem.observe(this, Observer {
            kotaDikirimDariAdapter = CityOngkirAutoCompleteAdapter(this, R.layout.item_city_ongkir_autocomplete)
            binding.txtKotaPengiriman.setAdapter(kotaDikirimDariAdapter)
            binding.txtKotaPengiriman.threshold = 1

            kotaDikirimDariAdapter.setData(it)
        })
    }

    private fun initBinding() {
        binding.viewModel = viewModel

        binding.btnRetry.setOnClickListener { viewModel.retry() }
        binding.edtTanggalKeberangkatan.afterTextChanged { viewModel.validateKeberangkatan(it) }
        binding.edtTanggalKembali.afterTextChanged { viewModel.validateKembali(it) }
        binding.edtTanggalPengiriman.afterTextChanged { viewModel.validatePengiriman(it) }
        binding.edtRincian.afterTextChanged { viewModel.validateRincian(it) }
        binding.txtKotaAsal.setOnItemClickListener { adapterView, _, position, _ -> viewModel.selectKotaAsal(adapterView.adapter.getItem(position) as CityItem) }
        binding.txtKotaTujuan.setOnItemClickListener { adapterView, _, position, _ -> viewModel.selectKotaTujuan(adapterView.adapter.getItem(position) as CityItem) }
        binding.txtKotaPengiriman.setOnItemClickListener { adapterView, _, position, _ -> viewModel.selectKotaPengiriman(adapterView.adapter.getItem(position) as CityOngkirItem) }
    }

    private fun initDialog() {
        loadingDialog = LoadingDialog(this)
        errorDialog = ErrorDialog(this)
        confirmationDialog = ConfirmationDialog(this)
        successDialog = SuccessDialog(this)

        viewModel.stateInit.observe(this, Observer { loadingDialog.loading(it == State.LOADING) })
        viewModel.errorMessageInit.observe(this, Observer { error(it) })

        viewModel.statePost.observe(this, Observer { loadingDialog.loading(it == State.LOADING) })
        viewModel.errorMessagePost.observe(this, Observer { error(it) })
        viewModel.successMessage.observe(this, Observer { success(it) })

        binding.btnConfirm.setOnClickListener {
            confirmationDialog.setConfirm {
                viewModel.postTrip()
                confirmationDialog.dismiss()
            }
            confirmationDialog.show()
        }
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
        successDialog.setSuccess { finish() }
        successDialog.show()
    }

}
