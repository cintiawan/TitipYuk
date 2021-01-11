package xyz.cintiawan.titipyuk.ui.chat

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_latest_chat.*
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.customview.LoadingDialog
import xyz.cintiawan.titipyuk.customview.WrapContentLinearLayoutManager
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.ui.chat.item.channel.ChannelViewAdapter
import xyz.cintiawan.titipyuk.util.State
import javax.inject.Inject

class LatestChatActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: LatestChatViewModel

    private lateinit var loadingDialog: LoadingDialog

    // Adapter
    lateinit var adapter: ChannelViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        setContentView(R.layout.activity_latest_chat)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Chat Yuk"

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
                .get(LatestChatViewModel::class.java)
        adapter = ChannelViewAdapter()

        viewModel.init { adapter.setData(it) }
    }

    private fun initBinding() {
        channel_list.adapter = adapter
        channel_list.layoutManager = WrapContentLinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun initDialog() {
        loadingDialog = LoadingDialog(this)

        viewModel.stateInit.observe(this, Observer { loadingDialog.loading(it == State.LOADING) })
    }

}
