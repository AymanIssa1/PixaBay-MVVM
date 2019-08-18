package com.example.pixabaymvvm.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pixabaymvvm.R
import com.example.pixabaymvvm.models.Hit
import com.example.pixabaymvvm.util.extensions.loadUrl
import kotlinx.android.synthetic.main.hit_item.view.*

class HitsAdapter(
    private var hits: ArrayList<Hit>,
    private val onItemClickListener: (hit: Hit, hitsViewHolder: HitViewModel) -> Unit
) :
    RecyclerView.Adapter<HitsAdapter.HitViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HitViewModel {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hit_item, parent, false)
        return HitViewModel(itemView)
    }

    override fun getItemCount(): Int {
        return hits.size
    }

    override fun onBindViewHolder(holder: HitViewModel, position: Int) {
        val hit = hits[position]
        holder.hitImageView.loadUrl(hit.webformatURL)
        holder.userImageView.loadUrl(hit.userImageURL)
        holder.userNameTextView.text = hit.user
        holder.downloadsTextView.text = hit.downloads.toString()
        holder.viewsTextView.text = hit.views.toString()
        holder.likesTextView.text = hit.likes.toString()

        holder.itemView.setOnClickListener { onItemClickListener.invoke(hit, holder) }
    }

    fun setHits(hits: ArrayList<Hit>) {
        this.hits = hits
    }

    inner class HitViewModel internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hitImageView: ImageView = itemView.hit_image_view
        val userImageView: ImageView = itemView.user_image_view
        val userNameTextView: TextView = itemView.user_name_text_view
        val downloadsTextView: TextView = itemView.downloads_text_view
        val viewsTextView: TextView = itemView.views_text_view
        val likesTextView: TextView = itemView.likes_text_view
    }

}