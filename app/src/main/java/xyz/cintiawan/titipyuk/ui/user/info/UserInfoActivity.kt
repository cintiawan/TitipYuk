package xyz.cintiawan.titipyuk.ui.user.info

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.databinding.ActivityUserInfoBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import javax.inject.Inject

class UserInfoActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: UserInfoViewModel

    private lateinit var binding: ActivityUserInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

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
                .get(UserInfoViewModel::class.java)
    }

    private fun initBinding() {
        binding.viewModel = viewModel
    }

    companion object {
        const val USER_ID = "user_id"
    }

}
