package com.example.pixabaymvvm.util.extensions

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.loadUrl(url: String?) {
    if (url != null && url.isNotEmpty())
        Picasso.get().load(url).into(this)
}

fun ImageView.loadUrl(url: String?, placeholder: Int) {
    if (url != null && url.isNotEmpty())
        Picasso.get()
            .load(url)
            .placeholder(placeholder)
            .into(this)
}