package xyz.cintiawan.titipyuk.util

import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import xyz.cintiawan.titipyuk.R
import java.io.File

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: LiveData<Boolean>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer {
            value -> view.setVisible(value?: false)
        })
    }
}

@BindingAdapter("mutableLoadingVisibility")
fun setMutableLoadingVisibility(view: View, visibility: LiveData<State>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer {
            value -> view.setVisible(value == State.LOADING)
        })
    }
}

@BindingAdapter("mutableRetryVisibility")
fun setMutableRetryVisibility(view: View, visibility: LiveData<State>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer {
            value -> view.setVisible(value == State.ERROR)
        })
    }
}

@BindingAdapter("mutableContentVisibility")
fun setMutableContentVisibility(view: View, visibility: LiveData<State>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer {
            value -> view.setVisible(value == State.DONE)
        })
    }
}

@BindingAdapter("mutableShimmerLoading")
fun setShimmerLoading(view: ShimmerFrameLayout, visibility: LiveData<Boolean>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value ->
            if(value) {
                view.startShimmer()
                view.visible()
            } else {
                view.stopShimmer()
                view.invisible()
            }
        })
    }
}

@BindingAdapter("mutableShimmerContentLoading")
fun setShimmerContentLoading(view: ShimmerFrameLayout, visibility: LiveData<State>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value ->
            if(value == State.LOADING) {
                view.startShimmer()
                view.visible()
            } else {
                view.stopShimmer()
                view.invisible()
            }
        })
    }
}

@BindingAdapter("mutableShimmerRecyclerViewContentLoading")
fun setShimmerRecyclerViewContentLoading(view: ShimmerRecyclerView, visibility: LiveData<State>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value ->
            if(value == State.LOADING) view.showShimmerAdapter() else view.hideShimmerAdapter()
        })
    }
}

@BindingAdapter("mutableEnable")
fun setMutableEnable(view: View, enable: LiveData<Boolean>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && enable != null) {
        enable.observe(parentActivity, Observer {
            value -> view.isEnabled = value
        })
    }
}

@BindingAdapter("mutableRefreshing")
fun setMutableRefreshing(view: SwipeRefreshLayout, visibility: LiveData<Boolean>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer {
            value -> view.isRefreshing = value?: false
        })
    }
}

@BindingAdapter("mutableText")
fun setMutableText(view: TextView, text: LiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && text != null) {
        text.observe(parentActivity, Observer {
            value -> view.text = value?: ""
        })
    }
}

@BindingAdapter("mutableEditText")
fun setMutableEditText(view: EditText, text: LiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && text != null) {
        text.observe(parentActivity, Observer {
            value -> view.setText(value?: "")
        })
    }
}

@BindingAdapter("mutableEditSelection")
fun setMutableEditSelection(view: EditText, sel: LiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && sel != null) {
        sel.observe(parentActivity, Observer {
            value -> view.setSelection(value)
        })
    }
}

@BindingAdapter("mutableButtonText")
fun setMutableButtonText(view: Button, text: LiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && text != null) {
        text.observe(parentActivity, Observer {
            value -> view.text = value?: ""
        })
    }
}

@BindingAdapter("mutableImage")
fun setMutableImage(view: ImageView, url: LiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && url != null) {
        url.observe(parentActivity, Observer {
            if(!it.isNullOrEmpty()) Picasso.get().load(it).placeholder(R.drawable.placeholder).into(view) else view.setImageResource(R.drawable.placeholder)
        })
    }
}

@BindingAdapter("mutableImagePath")
fun setMutableImagePath(view: ImageView, url: LiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && url != null) {
        url.observe(parentActivity, Observer {
            if(!it.isNullOrEmpty()) Picasso.get().load(File(it)).placeholder(R.drawable.placeholder).into(view) else view.setImageResource(android.R.color.transparent)
        })
    }
}

@BindingAdapter("mutableUserImage")
fun setMutableUserImage(view: ImageView, url: LiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && url != null) {
        url.observe(parentActivity, Observer {
            if(!it.isNullOrEmpty()) Picasso.get().load(it).placeholder(R.drawable.placeholder).into(view) else view.setImageResource(R.drawable.male)
        })
    }
}

@BindingAdapter("mutableStoryImage")
fun setMutableStoryImage(view: ImageView, url: LiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && url != null) {
        url.observe(parentActivity, Observer {
            if(!it.isNullOrEmpty()) Picasso.get().load(it).into(view)
        })
    }
}

@BindingAdapter("mutableCardBackgroundColor")
fun setMutableCardBackgroundColor(view: CardView, color: LiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && color != null) {
        color.observe(parentActivity, Observer {
            value -> view.setCardBackgroundColor(value!!)
        })
    }
}

@BindingAdapter("mutableErrorValidate")
fun setErrorValidate(view: TextInputLayout, error: LiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && error != null) {
        error.observe(parentActivity, Observer { value ->
            view.error = value
            view.isErrorEnabled = !value.isNullOrEmpty()
        })
    }
}

@BindingAdapter("mutableRatingNum")
fun setRatingNum(view: RatingBar, num: LiveData<Int>) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null) {
        num.observe(parentActivity, Observer { value ->
            view.numStars = value
            view.rating = value.toFloat()
        })
    }
}