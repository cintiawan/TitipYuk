package xyz.cintiawan.titipyuk.ui.slider

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import xyz.cintiawan.titipyuk.R
import xyz.cintiawan.titipyuk.base.BaseActivity
import kotlinx.android.synthetic.main.activity_image_full.*
import com.davemorrissey.labs.subscaleview.ImageSource
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception

class ImageFullActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_full)

        // Change status bar color
        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = Color.BLACK
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""

        if(intent.getBooleanExtra(IS_FILE, true)) {
            img_full.setImage(ImageSource.uri(intent.getStringExtra(SOURCE)))
        } else {
            Picasso.get().load(intent.getStringExtra(SOURCE)).into(object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) { }
                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) { }
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    bitmap?.let { img_full.setImage(ImageSource.bitmap(bitmap)) }
                }
            })
        }
        img_full.setTileBackgroundColor(Color.WHITE)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val SOURCE = "source"
        const val IS_FILE = "is_file"
    }

}
