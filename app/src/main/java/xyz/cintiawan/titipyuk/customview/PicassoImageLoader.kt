package xyz.cintiawan.titipyuk.customview

import android.widget.ImageView
import com.esafirm.imagepicker.features.imageloader.ImageLoader
import com.esafirm.imagepicker.features.imageloader.ImageType
import com.squareup.picasso.Picasso
import xyz.cintiawan.titipyuk.R
import java.io.File

class PicassoImageLoader : ImageLoader {

    companion object {
        const val WIDTH = 300
        const val HEIGHT = 300
    }

    override fun loadImage(path: String, imageView: ImageView, imageType: ImageType) {
        val placeholderResId = when(imageType) {
            ImageType.FOLDER -> R.drawable.gallery_image_placeholder
            else -> R.drawable.gallery_image_placeholder
        }

        Picasso.get()
                .load(File(path))
                .resize(WIDTH, HEIGHT)
                .centerCrop()
                .placeholder(placeholderResId)
                .error(placeholderResId)
                .into(imageView)
    }
}