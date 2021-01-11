package xyz.cintiawan.titipyuk.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_progress.*
import xyz.cintiawan.titipyuk.R

class LoadingDialog(context: Context, private val message: String = "Mohon Menunggu...")
    : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_progress)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setCanceledOnTouchOutside(false)

        txt_message.text = message
    }

    fun loading(loading: Boolean) = if(loading) show() else dismiss()
}