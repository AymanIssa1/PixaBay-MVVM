package com.example.pixabaymvvm.views

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar


/**
 * To fix the issue of re init the previous activity
 * when user click on toolbar back button
 */
class MyToolbar : Toolbar {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setNavigationOnClickListener(listener: OnClickListener?) {
        super.setNavigationOnClickListener {
            getActivity()!!.onBackPressed()
        }
    }

    private fun getActivity(): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }
}