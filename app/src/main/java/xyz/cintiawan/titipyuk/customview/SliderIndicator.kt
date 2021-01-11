package xyz.cintiawan.titipyuk.customview

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.viewpager.widget.ViewPager

class SliderIndicator(private val mContext: Context,
                      private val mContainer: LinearLayout?,
                      private val mViewPager: ViewPager?,
                      private var mDrawable: Int) : ViewPager.OnPageChangeListener {
    private var mSpacing = 0
    private var mSize = 0
    private var mPageCount = 0
    private var mPageScroll = true
    private var mInitialPage = 0

    private val defaultSizeInDp = 12
    private val defaultSpacingInDp = 12
    private val handler: Handler = Handler()

    fun setPageCount(pageCount: Int) {
        mPageCount = pageCount
    }

    fun setPageAutoScroll(scroll: Boolean) {
        mPageScroll = scroll
    }

    fun setInitialPage(page: Int) {
        mInitialPage = page
    }

    fun setDrawable(@DrawableRes drawable: Int) {
        mDrawable = drawable
    }

    fun setSpacingRes(@DimenRes spacingRes: Int) {
        mSpacing = spacingRes
    }

    fun setSize(@DimenRes dimenRes: Int) {
        mSize = dimenRes
    }

    fun show() {
        initIndicators()
        setIndicatorAsSelected(mInitialPage)
        if(mPageScroll) handler.postDelayed({ mViewPager?.currentItem = 1 }, 7500)
    }

    private fun initIndicators() {
        if (mContainer == null || mPageCount <= 0) {
            return
        }

        mViewPager?.addOnPageChangeListener(this)
        mContainer.removeAllViews()

        var view: View
        val dimen = if (mSize != 0) mContext.resources.getDimensionPixelSize(mSize) else mContext.resources.displayMetrics.density.toInt() * defaultSizeInDp
        val margin = if (mSpacing != 0) mContext.resources.getDimensionPixelSize(mSpacing) else mContext.resources.displayMetrics.density.toInt() * defaultSpacingInDp
        val lp = LinearLayout.LayoutParams(dimen, dimen)
        for (i in 0 until mPageCount) {
            view = View(mContext)
            lp.setMargins(if (i == 0) 0 else margin, 0, 0, 0)
            view.layoutParams = lp
            view.setBackgroundResource(mDrawable)
            view.isSelected = (i == 0)
            mContainer.addView(view)
        }
    }

    private fun setIndicatorAsSelected(index: Int) {
        if (mContainer == null) return
        for (i in 0 until mContainer.childCount) {
            val view = mContainer.getChildAt(i)
            view.isSelected = (i == index)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

    override fun onPageScrollStateChanged(state: Int) { }

    override fun onPageSelected(position: Int) {
        handler.removeCallbacksAndMessages(null)

        val index = position % mPageCount
        setIndicatorAsSelected(index)
        val moveTo = position + 1
        try {
            if(mPageScroll) handler.postDelayed({ mViewPager?.currentItem = moveTo }, 7500)
        } catch (e: Exception) { }
    }

    fun cleanup() {
        mViewPager?.clearOnPageChangeListeners()
    }
}