package xyz.cintiawan.titipyuk.ui.trip.verifikasi

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.customview.*
import xyz.cintiawan.titipyuk.databinding.ActivityVerifikasiTripBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.ui.trip.verifikasi.item.VerifikasiTripViewAdapter
import xyz.cintiawan.titipyuk.util.State
import javax.inject.Inject

class VerifikasiTripActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: VerifikasiTripViewModel

    private lateinit var binding: ActivityVerifikasiTripBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var errorDialog: ErrorDialog
    private lateinit var confirmationDialog: ConfirmationDialog
    private lateinit var successDialog: SuccessDialog

    // Adapter
    lateinit var adapter: VerifikasiTripViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verifikasi_trip)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Daftar Request Untuk Trip Saya"

        initViewModel()
        initBinding()
        initDialog()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initDialog() {
        loadingDialog = LoadingDialog(this)
        errorDialog = ErrorDialog(this)
        confirmationDialog = ConfirmationDialog(this)
        successDialog = SuccessDialog(this)

        viewModel.state.observe(this, Observer { loadingDialog.loading(it == State.LOADING) })
        viewModel.errorMessage.observe(this, Observer { error(it) })
        viewModel.successMessage.observe(this, Observer { success(it) })
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(VerifikasiTripViewModel::class.java)
        adapter = VerifikasiTripViewAdapter({ viewModel.retry() }, { id, respond ->
            confirmationDialog.setConfirm {
                viewModel.respond(id, respond)
                confirmationDialog.dismiss()
            }
            confirmationDialog.show()
        })

        viewModel.data.observe(this, Observer { adapter.updateList(it) })
        viewModel.state.observe(this, Observer { adapter.setState(it) })
        viewModel.successMessage
    }

    private fun initBinding() {
        binding.verifikasiList.adapter = adapter
        binding.verifikasiList.layoutManager = WrapContentLinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.viewModel = viewModel
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