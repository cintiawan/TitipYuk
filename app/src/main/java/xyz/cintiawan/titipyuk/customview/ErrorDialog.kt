package xyz.cintiawan.titipyuk.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_error.*
import xyz.cintiawan.titipyuk.R

class ErrorDialog(context: Context, private var message: String = "Mohon maaf, telah terjadi kesalahan", private var retry: () -> Unit = { })
    : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_error)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        txt_message.text = message
        btn_retry.setOnClickListener { retry() }
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun setRetry(retry: () -> Unit) {
        this.retry = retry
    }

}