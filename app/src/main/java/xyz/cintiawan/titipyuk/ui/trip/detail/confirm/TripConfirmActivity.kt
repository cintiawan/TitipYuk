package xyz.cintiawan.titipyuk.ui.trip.detail.confirm

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
import xyz.cintiawan.titipyuk.databinding.ActivityTripConfirmBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.model.item.AlamatItem
import xyz.cintiawan.titipyuk.model.item.CityItem
import xyz.cintiawan.titipyuk.model.item.KategoriItem
import xyz.cintiawan.titipyuk.ui.alamat.AlamatSelectBottomSheetFragment
import xyz.cintiawan.titipyuk.ui.alamat.item.list.AlamatCallback
import xyz.cintiawan.titipyuk.ui.city.item.autocomplete.CityAutocompleteAdapter
import xyz.cintiawan.titipyuk.ui.kategori.item.autocomplete.KategoriAutocompleteAdapter
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.afterTextChanged
import javax.inject.Inject

class TripConfirmActivity : BaseActivity(), AlamatCallback {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: TripConfirmViewModel

    private lateinit var binding: ActivityTripConfirmBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var errorDialog: ErrorDialog
    private lateinit var confirmationDialog: ConfirmationDialog
    private lateinit var successDialog: SuccessDialog
    private lateinit var alamatForm: AlamatSelectBottomSheetFragment

    // Adapter
    private lateinit var kategoriAdapter: KategoriAutocompleteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_confirm)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Kirim Request"
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

    override fun setAlamat(data: AlamatItem) {
        viewModel.setAlamat(data)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(TripConfirmViewModel::class.java)

        viewModel.kategoriItem.observe(this, Observer {
            kategoriAdapter = KategoriAutocompleteAdapter(this, R.layout.item_kategori_autocomplete)
            binding.txtKategori.setAdapter(kategoriAdapter)
            binding.txtKategori.threshold = 1

            kategoriAdapter.setData(it)
        })
    }

    private fun initBinding() {
        binding.viewModel = viewModel

        binding.btnPilihAlamat.setOnClickListener { loadAlamatSheet() }
        binding.btnRetry.setOnClickListener { viewModel.retry() }
        binding.btnPilihAlamat.setOnClickListener { loadAlamatSheet() }
        binding.edtNama.afterTextChanged { viewModel.validateNama(it) }
        binding.edtHarga.afterTextChanged { viewModel.validateHarga(it) }
        binding.edtJumlah.afterTextChanged { viewModel.validateJumlah(it) }
        binding.edtDeskripsi.afterTextChanged { viewModel.validateDeskripsi(it) }
        binding.txtKategori.setOnItemClickListener { adapterView, _, position, _ -> viewModel.selectKategori(adapterView.adapter.getItem(position) as KategoriItem) }
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
                viewModel.postConfirmation(intent.getIntExtra(ID, 0))
                confirmationDialog.dismiss()
            }
            confirmationDialog.show()
        }
    }

    private fun loadAlamatSheet() {
        alamatForm = AlamatSelectBottomSheetFragment(this)
        alamatForm.show(supportFragmentManager, AlamatSelectBottomSheetFragment::class.simpleName)
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

    companion object {
        const val ID = "id"
    }

}
