package xyz.cintiawan.titipyuk.ui.user.profile.setting

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.activity_user_setting.*
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import xyz.cintiawan.titipyuk.customview.ConfirmationDialog
import xyz.cintiawan.titipyuk.customview.ErrorDialog
import xyz.cintiawan.titipyuk.customview.LoadingDialog
import xyz.cintiawan.titipyuk.customview.SuccessDialog
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.ui.alamat.SettingAlamatActivity
import xyz.cintiawan.titipyuk.ui.splash.SplashActivity
import xyz.cintiawan.titipyuk.util.SPUtil
import xyz.cintiawan.titipyuk.util.State
import javax.inject.Inject

class UserSettingActivity : BaseActivity() {
    @Inject lateinit var factory: ViewModelFactory
    @Inject lateinit var sp: SharedPreferences
    @Inject lateinit var googleClient: GoogleSignInClient
    private lateinit var viewModel: UserSettingViewModel

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var errorDialog: ErrorDialog
    private lateinit var confirmationDialog: ConfirmationDialog
    private lateinit var successDialog: SuccessDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.inject()
        setContentView(R.layout.activity_user_setting)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Pengaturan Akun"

        viewModel = ViewModelProviders
                .of(this, factory)
                .get(UserSettingViewModel::class.java)

        lyt_profile.setOnClickListener { startActivity(Intent(this, SettingProfileActivity::class.java)) }
        lyt_password.setOnClickListener { startActivity(Intent(this, SettingPasswordActivity::class.java)) }
        lyt_alamat.setOnClickListener { startActivity(Intent(this, SettingAlamatActivity::class.java)) }

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

        lyt_logout.setOnClickListener {
            confirmationDialog.setMessage("Apakah Anda yakin ingin Logout?")
            confirmationDialog.setConfirm {
                viewModel.logout {
                    googleClient.signOut()
                    SPUtil.removeAccessToken(sp)
                }
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
        successDialog.setSuccess {
            startActivity(Intent(this, SplashActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            or Intent.FLAG_ACTIVITY_NEW_TASK))
        }
        successDialog.show()
    }

}
