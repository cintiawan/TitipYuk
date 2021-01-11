package xyz.cintiawan.titipyuk.ui.request.post

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.customview.*
import xyz.cintiawan.titipyuk.databinding.ActivityPostRequestBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.model.item.AlamatItem
import xyz.cintiawan.titipyuk.model.item.CityItem
import xyz.cintiawan.titipyuk.model.item.KategoriItem
import xyz.cintiawan.titipyuk.ui.alamat.item.list.AlamatCallback
import xyz.cintiawan.titipyuk.ui.alamat.AlamatSelectBottomSheetFragment
import xyz.cintiawan.titipyuk.ui.city.item.autocomplete.CityAutocompleteAdapter
import xyz.cintiawan.titipyuk.ui.kategori.item.autocomplete.KategoriAutocompleteAdapter
import xyz.cintiawan.titipyuk.ui.slider.ImageFullActivity
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.afterTextChanged
import xyz.cintiawan.titipyuk.util.gone
import xyz.cintiawan.titipyuk.util.visible
import java.io.File
import javax.inject.Inject

class PostRequestActivity : BaseActivity(), AlamatCallback {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: PostRequestViewModel

    private lateinit var binding: ActivityPostRequestBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var errorDialog: ErrorDialog
    private lateinit var confirmationDialog: ConfirmationDialog
    private lateinit var successDialog: SuccessDialog
    private lateinit var imgMenu: AlertDialog
    private lateinit var alamatForm: AlamatSelectBottomSheetFragment

    // Adapter
    private lateinit var kotaAsalAdapter: CityAutocompleteAdapter
    private lateinit var kategoriAdapter: KategoriAutocompleteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_request)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Post Request"
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            resetImage()

            val datas = HashMap<Int, String>()
            var count = 1
            ImagePicker.getImages(data).forEach {
                datas[count] = it.path
                count++
            }
            if(datas.size > 0) {
                binding.layoutImgPrev.gone()
                binding.layoutImgAfter.visible()
            } else {
                binding.layoutImgPrev.visible()
                binding.layoutImgAfter.gone()
            }

            datas.forEach { value ->
                Compressor(this)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(50)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .compressToFileAsFlowable(File(value.value))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ file ->
                            when(value.key) {
                                1 -> {
                                    viewModel.gambar1Path.value = file.path
                                    binding.imgSatu.setOnClickListener { openListDialog(value.key, file.path) }
                                }
                                2 -> {
                                    viewModel.gambar2Path.value = file.path
                                    binding.imgDua.setOnClickListener { openListDialog(value.key, file.path) }
                                }
                                3 -> {
                                    viewModel.gambar3Path.value = file.path
                                    binding.imgTiga.setOnClickListener { openListDialog(value.key, file.path) }
                                }
                                4 -> {
                                    viewModel.gambar4Path.value = file.path
                                    binding.imgEmpat.setOnClickListener { openListDialog(value.key, file.path) }
                                }
                                5 -> {
                                    viewModel.gambar5Path.value = file.path
                                    binding.imgLima.setOnClickListener { openListDialog(value.key, file.path) }
                                }
                            }
                            viewModel.checkButton()
                        }, { })
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun setAlamat(data: AlamatItem) {
        viewModel.setAlamat(data)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(PostRequestViewModel::class.java)

        viewModel.kotaItem.observe(this, Observer {
            kotaAsalAdapter = CityAutocompleteAdapter(this, R.layout.item_city_autocomplete)
            binding.txtKotaAsal.setAdapter(kotaAsalAdapter)
            binding.txtKotaAsal.threshold = 1

            kotaAsalAdapter.setData(it)
        })

        viewModel.kategoriItem.observe(this, Observer {
            kategoriAdapter = KategoriAutocompleteAdapter(this, R.layout.item_kategori_autocomplete)
            binding.txtKategori.setAdapter(kategoriAdapter)
            binding.txtKategori.threshold = 1

            kategoriAdapter.setData(it)
        })
    }

    private fun initBinding() {
        binding.viewModel = viewModel

        binding.layoutTambahGambar.setOnClickListener { loadGallery() }
        binding.btnRetry.setOnClickListener { viewModel.retry() }
        binding.btnTambahGambar.setOnClickListener { loadGallery() }
        binding.btnPilihAlamat.setOnClickListener { loadAlamatSheet() }
        binding.edtNama.afterTextChanged { viewModel.validateNama(it) }
        binding.edtHarga.afterTextChanged { viewModel.validateHarga(it) }
        binding.edtJumlah.afterTextChanged { viewModel.validateJumlah(it) }
        binding.edtTanggalDitutup.afterTextChanged { viewModel.validateTanggalDitutup(it) }
        binding.edtDeskripsi.afterTextChanged { viewModel.validateDeskripsi(it) }
        binding.txtKotaAsal.setOnItemClickListener { adapterView, _, position, _ -> viewModel.selectKotaAsal(adapterView.adapter.getItem(position) as CityItem) }
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
                viewModel.postRequest()
                confirmationDialog.dismiss()
            }
            confirmationDialog.show()
        }
    }

    private fun loadGallery() {
        ImagePicker.create(this)
                .returnMode(ReturnMode.NONE)
                .toolbarFolderTitle("Pilih Folder")
                .toolbarImageTitle("Pilih Maksimal 5 Foto")
                .toolbarArrowColor(Color.WHITE)
                .includeVideo(false)
                .limit(5)
                .imageDirectory("Titip Yuk")
                .toolbarDoneButtonText("Selesai")
                .theme(R.style.ImagePickerTheme)
                .enableLog(true)
                .imageLoader(PicassoImageLoader())
                .start()
    }

    private fun openListDialog(id: Int, img: String?) {
        if(!img.isNullOrEmpty()) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Pilih Opsi")

            val menus = arrayOf("Lihat Gambar", "Hapus Gambar")
            builder.setItems(menus) { _, which ->
                when (which) {
                    0 -> {
                        startActivity(Intent(this, ImageFullActivity::class.java)
                                .putExtra(ImageFullActivity.SOURCE, img)
                                .putExtra(ImageFullActivity.IS_FILE, true))
                    }
                    1 -> {
                        hapusImage(id)
                        viewModel.checkButton()
                    }
                }
            }

            imgMenu = builder.create()
            imgMenu.show()
        }
    }

    private fun hapusImage(id: Int) {
        when(id) {
            1 -> {
                viewModel.gambar1Path.value = viewModel.gambar2Path.value
                viewModel.gambar2Path.value = viewModel.gambar3Path.value
                viewModel.gambar3Path.value = viewModel.gambar4Path.value
                viewModel.gambar4Path.value = viewModel.gambar5Path.value
                viewModel.gambar5Path.value = ""
                binding.imgSatu.setOnClickListener { openListDialog(1, viewModel.gambar1Path.value) }
                binding.imgDua.setOnClickListener { openListDialog(2, viewModel.gambar2Path.value) }
                binding.imgTiga.setOnClickListener { openListDialog(3, viewModel.gambar3Path.value) }
                binding.imgEmpat.setOnClickListener { openListDialog(4, viewModel.gambar4Path.value) }
                binding.imgLima.setOnClickListener { }
            }
            2 -> {
                viewModel.gambar2Path.value = viewModel.gambar3Path.value
                viewModel.gambar3Path.value = viewModel.gambar4Path.value
                viewModel.gambar4Path.value = viewModel.gambar5Path.value
                viewModel.gambar5Path.value = ""
                binding.imgDua.setOnClickListener { openListDialog(2, viewModel.gambar2Path.value) }
                binding.imgTiga.setOnClickListener { openListDialog(3, viewModel.gambar3Path.value) }
                binding.imgEmpat.setOnClickListener { openListDialog(4, viewModel.gambar4Path.value) }
                binding.imgLima.setOnClickListener { }
            }
            3 -> {
                viewModel.gambar3Path.value = viewModel.gambar4Path.value
                viewModel.gambar4Path.value = viewModel.gambar5Path.value
                viewModel.gambar5Path.value = ""
                binding.imgTiga.setOnClickListener { openListDialog(3, viewModel.gambar3Path.value) }
                binding.imgEmpat.setOnClickListener { openListDialog(4, viewModel.gambar4Path.value) }
                binding.imgLima.setOnClickListener { }
            }
            4 -> {
                viewModel.gambar4Path.value = viewModel.gambar5Path.value
                viewModel.gambar5Path.value = ""
                binding.imgEmpat.setOnClickListener { openListDialog(4, viewModel.gambar4Path.value) }
                binding.imgLima.setOnClickListener { }
            }
            5 -> {
                viewModel.gambar5Path.value = ""
                binding.imgLima.setOnClickListener { }
            }
        }
    }

    private fun resetImage() {
        viewModel.gambar1Path.value = ""
        viewModel.gambar2Path.value = ""
        viewModel.gambar3Path.value = ""
        viewModel.gambar4Path.value = ""
        viewModel.gambar5Path.value = ""

        binding.imgSatu.setOnClickListener {  }
        binding.imgDua.setOnClickListener {  }
        binding.imgTiga.setOnClickListener {  }
        binding.imgEmpat.setOnClickListener {  }
        binding.imgLima.setOnClickListener {  }
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

}
