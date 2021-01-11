package xyz.cintiawan.titipyuk.ui.user.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseFragment
import xyz.cintiawan.titipyuk.customview.LoadingDialog
import xyz.cintiawan.titipyuk.databinding.FragmentRegisterBinding
import xyz.cintiawan.titipyuk.di.ViewModelFactory
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
class RegisterFragment : BaseFragment() {
    @Inject lateinit var factory: ViewModelFactory
    private lateinit var viewModel: RegisterViewModel

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var snackbar: Snackbar
    private lateinit var dialog: LoadingDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
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

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, factory)
                .get(RegisterViewModel::class.java)

        viewModel.successMessage.observe(this, Observer {
            snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok) { }
            (snackbar.view.findViewById(R.id.snackbar_text) as TextView)
                    .setTextColor(ContextCompat.getColor(context!!, R.color.colorLightGrey))
            snackbar.show()
        })

        viewModel.errorMessage.observe(this, Observer {
            snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
                    .setAction(R.string.ok) { }
            (snackbar.view.findViewById(R.id.snackbar_text) as TextView)
                    .setTextColor(ContextCompat.getColor(context!!, R.color.colorLightGrey))
            snackbar.show()
        })
    }

    private fun initDialog() {
        dialog = LoadingDialog(context!!)

        viewModel.state.observe(this, Observer { if(it == State.LOADING) dialog.show() else dialog.dismiss() })
    }

    private fun initBinding() {
        binding.viewModel = viewModel
        binding.edtEmail.afterTextChanged { viewModel.validateEmail(it) }
        binding.edtNama.afterTextChanged { viewModel.validateName(it) }
        binding.edtPassword.afterTextChanged { viewModel.validatePassword(it) }
        binding.edtPasswordKonfirmasi.afterTextChanged { viewModel.validatePasswordConf(it) }
        binding.edtTelepon.afterTextChanged { viewModel.validatePhoneNumber(it) }
        binding.btnRegistrasi.setOnClickListener { viewModel.register() }
    }

}
