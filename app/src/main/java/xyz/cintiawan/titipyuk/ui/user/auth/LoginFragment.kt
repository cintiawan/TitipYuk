package xyz.cintiawan.titipyuk.ui.user.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseFragment
import xyz.cintiawan.titipyuk.customview.LoadingDialog
import xyz.cintiawan.titipyuk.databinding.FragmentLoginBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
import xyz.cintiawan.titipyuk.ui.splash.SplashActivity
import xyz.cintiawan.titipyuk.util.AnimType
import xyz.cintiawan.titipyuk.util.SPUtil
import xyz.cintiawan.titipyuk.util.State
import xyz.cintiawan.titipyuk.util.afterTextChanged
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : BaseFragment() {
    @Inject lateinit var factory: ViewModelFactory
    @Inject lateinit var sp: SharedPreferences
    @Inject lateinit var toast: Toast
    @Inject lateinit var googleClient: GoogleSignInClient
    private lateinit var viewModel: LoginViewModel

    private lateinit var binding: FragmentLoginBinding
    private lateinit var snackbar: Snackbar
    private lateinit var dialog: LoadingDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.inject()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
        initDialog()
        initBinding()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                viewModel.googleLogin(account)
            } catch (e: ApiException) {
                Log.e("apalah_ini_error", e.message)
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(LoginViewModel::class.java)

        viewModel.message.observe(this, Observer {
            if(!it.isNullOrEmpty()) {
                SPUtil.setAccessToken(sp, it)

                // Restart App
                toast.setText("Anda telah berhasil login")
                toast.duration = Toast.LENGTH_LONG
                toast.show()
                startActivity(Intent(context, SplashActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                or Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        })

        viewModel.errorMessage.observe(this, Observer {
            if(!it.isNullOrEmpty()) {
                snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
                        .setAction(R.string.ok) { }
                (snackbar.view.findViewById(R.id.snackbar_text) as TextView)
                        .setTextColor(ContextCompat.getColor(context!!, R.color.colorLightGrey))
                snackbar.show()
            }
        })
    }

    private fun initDialog() {
        dialog = LoadingDialog(context!!)

        viewModel.state.observe(this, Observer { if(it == State.LOADING) dialog.show() else dialog.dismiss() })
    }

    private fun initBinding() {
        binding.viewModel = viewModel
        binding.edtEmail.afterTextChanged { viewModel.validateEmail(it) }
        binding.edtPassword.afterTextChanged { viewModel.validatePassword(it) }
        binding.btnLogin.setOnClickListener { viewModel.login() }
        binding.btnGoogle.setOnClickListener { startActivityForResult(googleClient.signInIntent, RC_GOOGLE_SIGN_IN) }
        binding.btnRegistrasi.setOnClickListener {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.addToBackStack(null)
                    ?.setCustomAnimations(AnimType.FADE.getAnimPair().first, AnimType.FADE.getAnimPair().second)
                    ?.replace(R.id.container, RegisterFragment(), RegisterFragment::class.simpleName)
                    ?.commit()
            viewModel.reset()
        }
    }

    companion object {
        const val RC_GOOGLE_SIGN_IN = 1
    }

}
