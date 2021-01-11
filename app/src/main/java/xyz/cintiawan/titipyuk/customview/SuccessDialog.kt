package xyz.cintiawan.titipyuk.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_success.*
import xyz.cintiawan.titipyuk.R

class SuccessDialog(context: Context, private var message: String = "Berhasil menyimpan", private var success: () -> Unit = { })
    : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_success)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setCanceledOnTouchOutside(false)

        txt_message.text = message
        btn_success.setOnClickListener { success() }
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun setSuccess(success: () -> Unit) {
        this.success = success
    }

}