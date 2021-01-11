package xyz.cintiawan.titipyuk.ui.alamat.post

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
import xyz.cintiawan.titipyuk.databinding.ActivityPostAlamatBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.model.item.CityOngkirItem
import xyz.cintiawan.titipyuk.ui.city.item.autocomplete.CityOngkirAutoCompleteAdapter
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.afterTextChanged
import javax.inject.Inject

class PostAlamatActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: PostAlamatViewModel

    private lateinit var binding: ActivityPostAlamatBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var errorDialog: ErrorDialog
    private lateinit var confirmationDialog: ConfirmationDialog
    private lateinit var successDialog: SuccessDialog

    // Adapter
    private lateinit var kotaDikirimKeAdapter: CityOngkirAutoCompleteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_alamat)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Buat Alamat Baru"
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
                .get(PostAlamatViewModel::class.java)

        viewModel.kotaOngkirItem.observe(this, Observer {
            kotaDikirimKeAdapter = CityOngkirAutoCompleteAdapter(this, R.layout.item_city_ongkir_autocomplete)
            binding.txtKotaDikirim.setAdapter(kotaDikirimKeAdapter)
            binding.txtKotaDikirim.threshold = 1

            kotaDikirimKeAdapter.setData(it)
        })
    }

    private fun initBinding() {
        binding.viewModel = viewModel

        binding.btnRetry.setOnClickListener { viewModel.retry() }
        binding.txtKotaDikirim.setOnItemClickListener { adapterView, _, position, _ -> viewModel.selectKotaDikirim(adapterView.adapter.getItem(position) as CityOngkirItem) }
        binding.edtKodePos.afterTextChanged { viewModel.validateKodePos(it) }
        binding.edtAlamat.afterTextChanged { viewModel.validateJalan(it) }
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
                viewModel.simpanAlamat()
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
