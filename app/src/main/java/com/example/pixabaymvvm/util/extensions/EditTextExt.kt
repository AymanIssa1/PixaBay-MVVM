package com.example.pixabaymvvm.util.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(
        this.currentFocus?.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}