package com.example.pixabaymvvm.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.airbnb.paris.extensions.style
import com.example.pixabaymvvm.R
import kotlinx.android.synthetic.main.info_banner_view.view.*


class BannerInfoView : CoordinatorLayout {

    enum class BannerState { NO_INTERNET, INTERNET_CONNECTED, INFO }

    private var _bannerState: BannerState? = BannerState.INFO

    private var _titleText: String? = ""
    private var _descriptionText: String? = ""

    /**
     * The text to draw
     */
    var titleText: String?
        get() = _titleText
        set(value) {
            _titleText = value
            titleTextView.text = value
        }

    /**
     * The text to draw
     */
    var descriptionText: String?
        get() = _descriptionText
        set(value) {
            _descriptionText = value
            descriptionTextView.text = value
        }

    var bannerState: BannerState?
        get() = _bannerState
        set(value) {
            _bannerState = value
        }


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int, bannerState: BannerState = BannerState.INFO) {
        View.inflate(context, R.layout.info_banner_view, this)
        setStyle(bannerState)
    }

    fun setStyle(bannerState: BannerState) {
        this.bannerState = bannerState
        when (bannerState) {
            BannerState.NO_INTERNET -> {
                titleTextView.style(R.style.statusErrorTitleTextStyle)
                descriptionTextView.style(R.style.statusErrorDescriptionTextStyle)
                root.style(R.style.statusErrorBackgroundStyle)
            }
            BannerState.INTERNET_CONNECTED -> {
                titleTextView.style(R.style.statusSuccessTitleTextStyle)
                descriptionTextView.style(R.style.statusSuccessDescriptionTextStyle)
                root.style(R.style.statusSuccessBackgroundStyle)
            }
            BannerState.INFO -> {
                titleTextView.style(R.style.statusInfoTitleTextStyle)
                descriptionTextView.style(R.style.statusInfoDescriptionTextStyle)
                root.style(R.style.statusInfoBackgroundStyle)
            }
        }
    }

    fun dismiss() = this.collapse()
    fun show() = this.expand()

    private fun View.expand() {
        this@expand.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targetHeight = this@expand.measuredHeight

        this@expand.layoutParams.height = 0
        this@expand.visibility = View.VISIBLE
        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                this@expand.layoutParams.height = if (interpolatedTime == 1f)
                    ViewGroup.LayoutParams.WRAP_CONTENT
                else
                    (targetHeight * interpolatedTime).toInt()
                this@expand.requestLayout()
            }

            override fun willChangeBounds(): Boolean = true
        }

        animation.duration = (targetHeight / this@expand.context.resources.displayMetrics.density).toInt().toLong() * 4
        this@expand.startAnimation(animation)
    }

    private fun View.collapse() {
        val initialHeight = this.measuredHeight

        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    this@collapse.visibility = View.GONE
                } else {
                    this@collapse.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    this@collapse.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean = true
        }

        animation.duration = (initialHeight / this.context.resources.displayMetrics.density).toInt().toLong() * 4
        this.startAnimation(animation)
    }

}
