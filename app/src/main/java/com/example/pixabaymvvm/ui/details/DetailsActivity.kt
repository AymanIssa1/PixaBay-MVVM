package com.example.pixabaymvvm.ui.details

import android.os.Bundle
import com.example.pixabaymvvm.HIT_EXTRA
import com.example.pixabaymvvm.R
import com.example.pixabaymvvm.models.Hit
import com.example.pixabaymvvm.ui.BaseActivity
import com.example.pixabaymvvm.util.extensions.loadUrl
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*

class DetailsActivity : BaseActivity() {

    private lateinit var hit: Hit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        hit = intent.getParcelableExtra(HIT_EXTRA) as Hit
        hit_image_view.loadUrl(hit.webformatURL)
        user_image_view.loadUrl(hit.userImageURL)
        user_name_text_view.text = hit.user
        downloads_text_view.text = "${hit.downloads} ${getString(R.string.downloads)}"
        views_text_view.text = "${hit.views} ${getString(R.string.views)}"
        likes_text_view.text = "${hit.likes} ${getString(R.string.likes)}"

        val tags = hit.tags.split(", ")
        tags.forEach { addChip(it) }

    }

    private fun addChip(text: String) {
        val chip = Chip(chips_group.context)
        chip.text = text
        chip.isChecked = true
        chip.isCheckable = false
        chips_group.addView(chip)
    }

}
