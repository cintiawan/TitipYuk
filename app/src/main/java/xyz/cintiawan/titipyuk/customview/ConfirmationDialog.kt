package xyz.cintiawan.titipyuk.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_confirmation.*
import xyz.cintiawan.titipyuk.R

class ConfirmationDialog(context: Context,
                         private var message: String = "Konfirmasi postingan?",
                         private var confirm: () -> Unit = { },
                         private var cancel: () -> Unit = { })
    : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_confirmation)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        cancel = { dismiss() }
        txt_message.text = message
        btn_yes.setOnClickListener { confirm() }
        btn_no.setOnClickListener { cancel() }
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun setConfirm(confirm: () -> Unit) {
        this.confirm = confirm
    }

    fun setCancel(cancel: () -> Unit) {
        this.cancel = cancel
    }

}