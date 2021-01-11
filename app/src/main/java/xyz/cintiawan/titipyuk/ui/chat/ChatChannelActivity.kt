package xyz.cintiawan.titipyuk.ui.chat

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.customview.LoadingDialog
import xyz.cintiawan.titipyuk.customview.PicassoImageLoader
import xyz.cintiawan.titipyuk.customview.WrapContentLinearLayoutManager
import xyz.cintiawan.titipyuk.databinding.ActivityChatChannelBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.ui.chat.item.chat.ChatMessageViewAdapter
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.afterTextChanged
import java.io.File
import javax.inject.Inject

class ChatChannelActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: ChatChannelViewModel

    private lateinit var binding: ActivityChatChannelBinding
    private lateinit var loadingDialog: LoadingDialog
    private var otherUserId = ""

    // Adapter
    lateinit var adapter: ChatMessageViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_channel)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        otherUserId = intent.getStringExtra(OTHER_USER_ID)

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
            val image = ImagePicker.getFirstImageOrNull(data)
            Compressor(this)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .compressToFileAsFlowable(File(image.path))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ file ->
                        viewModel.validateImage(file.path)
                        viewModel.sendImage(otherUserId)
                    }, { })
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(ChatChannelViewModel::class.java)
        adapter = ChatMessageViewAdapter()

        viewModel.init(otherUserId, {
            adapter.setData(it)
            binding.messageList.scrollToPosition(adapter.itemCount - 1)
        }, {
            binding.btnSend.setOnClickListener {
                viewModel.sendText(otherUserId)
                binding.edtMessage.text?.clear()
            }
            binding.fabImage.setOnClickListener { loadGallery() }
        })
    }

    private fun initBinding() {
        binding.messageList.adapter = adapter
        binding.messageList.layoutManager = WrapContentLinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.viewModel = viewModel
        binding.edtMessage.afterTextChanged { viewModel.validateText(it) }
    }

    private fun initDialog() {
        loadingDialog = LoadingDialog(this)

        viewModel.stateInit.observe(this, Observer { loadingDialog.loading(it == State.LOADING) })
    }

    private fun loadGallery() {
        ImagePicker.create(this)
                .returnMode(ReturnMode.NONE)
                .toolbarFolderTitle("Pilih Folder")
                .toolbarImageTitle("Pilih Foto Untuk Dikirim")
                .toolbarArrowColor(Color.WHITE)
                .includeVideo(false)
                .single()
                .imageDirectory("Titip Yuk")
                .toolbarDoneButtonText("Kirim")
                .theme(R.style.ImagePickerTheme)
                .enableLog(true)
                .imageLoader(PicassoImageLoader())
                .start()
    }

    companion object {
        const val OTHER_USER_ID = "other_user_id"
    }

}
