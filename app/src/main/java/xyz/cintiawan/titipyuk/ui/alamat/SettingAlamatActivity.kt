package xyz.cintiawan.titipyuk.ui.alamat

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.customview.WrapContentLinearLayoutManager
import xyz.cintiawan.titipyuk.databinding.ActivitySettingAlamatBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.ui.alamat.item.list.AlamatViewAdapter
import xyz.cintiawan.titipyuk.ui.alamat.post.PostAlamatActivity
import javax.inject.Inject

class SettingAlamatActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: AlamatViewModel

    private lateinit var binding: ActivitySettingAlamatBinding

    // Adapter
    lateinit var adapter: AlamatViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_alamat)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Daftar Alamat Saya"

        initViewModel()
        initBinding()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(AlamatViewModel::class.java)
        adapter = AlamatViewAdapter()

        viewModel.data.observe(this, Observer { adapter.updateList(it) })
        viewModel.state.observe(this, Observer { adapter.setState(it) })
    }

    private fun initBinding() {
        binding.alamatList.adapter = adapter
        binding.alamatList.layoutManager = WrapContentLinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.viewModel = viewModel
        binding.btnAlamatBaru.setOnClickListener { startActivity(Intent(this, PostAlamatActivity::class.java)) }
    }

}
