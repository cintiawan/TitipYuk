package xyz.cintiawan.titipyuk.util

import android.content.ContextWrapper
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Response
import xyz.cintiawan.titipyuk.model.Message
import java.io.File
import java.lang.NumberFormatException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun View.getParentActivity() : AppCompatActivity? {
    var context = this.context
    while(context is ContextWrapper) {
        if(context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.setVisible(visible: Boolean) {
    visibility = if(visible) View.VISIBLE else View.GONE
}

fun String.stringToDate(): String {
    val cal = Calendar.getInstance()
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        cal.time = sdf.parse(this)
    } catch (e: ParseException) {
        cal.timeInMillis = 0
        Log.e("parse error", this)
    }

    val format = SimpleDateFormat("dd MMM yy", Locale.getDefault())
    return format.format(cal.time)
}

fun Long.millisToDate(): String {
    val sdf = SimpleDateFormat("dd MMM yy", Locale.getDefault())
    return sdf.format((Date(this)))
}

fun Long.millisToDateTime(): String {
    val sdf = SimpleDateFormat("dd MMM yy, HH:mm", Locale.getDefault())
    return sdf.format((Date(this)))
}

fun String.dateToMillis(): Long {
    val cal = Calendar.getInstance()
    try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        cal.time = sdf.parse(this)
    } catch (e: ParseException) {
        cal.timeInMillis = 0
        Log.e("parse error", this)
    }

    return cal.timeInMillis
}

fun Int.toJumlahFormat(): String {
    return this.toString() + " Pcs"
}

fun Double?.toRupiahFormat(): String {
    val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
    val formatRp = DecimalFormatSymbols(Locale.GERMAN)

    formatRp.currencySymbol = "Rp "
    formatRp.decimalSeparator = '.'
    formatRp.groupingSeparator = '.'

    kursIndonesia.decimalFormatSymbols = formatRp
    kursIndonesia.maximumFractionDigits = 0
    return kursIndonesia.format(this)
}

fun Double?.toRupiahFormatNoSymbol(): String {
    val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
    val formatRp = DecimalFormatSymbols(Locale.GERMAN)

    formatRp.currencySymbol = ""
    formatRp.decimalSeparator = '.'
    formatRp.groupingSeparator = '.'

    kursIndonesia.decimalFormatSymbols = formatRp
    kursIndonesia.maximumFractionDigits = 0
    return kursIndonesia.format(this)
}

fun String?.toHargaDoubleFormat(): Double {
    try {
        this?.let {
            return it.replace("[^\\d.]|\\.", "")
                    .replace("Rp ", "")
                    .replace(".", "")
                    .replace(",", "")
                    .toDouble()
        }
    } catch (e: NumberFormatException) {
        return 0.0
    }
    return 0.0
}

fun Int?.toKilogramFormat(): String {
    return this.toString() + " g"
}

fun String?.toBeratFloatFormat(): Int {
    try {
        this?.let {
            return it.replace("[^\\d.]|\\.", "")
                    .replace(" g", "")
                    .replace(".0", "")
                    .toInt()
        }
    } catch (e: NumberFormatException) {
        return 0
    }
    return 0
}

fun Long.getRemainingTime(): Long = this - System.currentTimeMillis()
fun Long.getPassedTime(): Long = System.currentTimeMillis() - this

fun Long.toDays(): Long = this / 86400000
fun Long.toHours(): Long = (this / 3600000) - (this.toDays() * 24)
fun Long.toMinutes(): Long = (this / 60000) - ((this.toDays() * 24 * 60) + (this.toHours() * 60))
fun Long.toSeconds(): Long = (this / 1000) - ((this.toDays() * 24 * 60 * 60) + (this.toHours() * 60 * 60) + (this.toMinutes() * 60))

fun String.toHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }
}

fun Any.toRequestBody(): RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), this.toString())

fun String.toMultipartBody(name: String): MultipartBody.Part? {
    if(this.isNotEmpty()) {
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), File(this))
        return MultipartBody.Part.createFormData(name, "image.webp", requestBody)
    }
    return null
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun HttpException.toErrorMessage(): String {
    return JsonParser().parse(this.response().errorBody()?.string())
            .asJsonObject["message"]
            .asString
}